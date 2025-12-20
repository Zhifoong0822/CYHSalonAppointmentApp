package com.example.cyhsalonappointment.screens.Booking

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.cyhsalonappointment.AppointmentReminderWorker
import com.example.cyhsalonappointment.local.DAO.AppointmentDao
import com.example.cyhsalonappointment.local.DAO.TimeSlotDao
import com.example.cyhsalonappointment.local.entity.Appointment
import com.example.cyhsalonappointment.screens.BookingHistory.getStartTimeFromSlot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class BookingViewModel(
    private val appointmentDao: AppointmentDao,
    private val timeSlotDao: TimeSlotDao
) : ViewModel() {

    private val _saveResult = MutableStateFlow<Boolean?>(null)
    val saveResult = _saveResult

    // ---------- ID GENERATOR ----------
    private suspend fun generateAppointmentId(): String {
        val count = appointmentDao.getAppointmentCount()
        return "APP" + String.format("%04d", count + 1)
    }


    // ---------- NEW FUNCTION (USED BY PAYMENT) ----------
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createAppointmentAndReturnId(
        date: String,
        timeSlotId: String,
        customerId: String?,
        serviceId: Int,
        stylistId: String?
    ): String {

        val newId = generateAppointmentId()
        val normalizedDate = LocalDate.parse(date).toString()

        val appointment = Appointment(
            appointmentId = newId,
            appointmentDate = normalizedDate,
            timeSlotId = timeSlotId,
            customerId = customerId,
            serviceId = serviceId,
            stylistId = stylistId
        )

        appointmentDao.insertAppointment(appointment)
        return newId
    }

    // ---------- NOTIFICATION (UNCHANGED) ----------
    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleNotification(
        context: Context,
        appointmentDateTime: LocalDateTime,
        appointmentId: String
    ) {
        val reminderTime = appointmentDateTime.minusHours(1)
        val now = LocalDateTime.now()

        if (reminderTime.isBefore(now)) {
            // Too late to schedule reminder
            return
        }

        val delayMillis = Duration.between(now, reminderTime).toMillis()

        val workRequest = OneTimeWorkRequestBuilder<AppointmentReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    "message" to "Your appointment is in 1 hour.",
                    "appointmentId" to appointmentId
                )
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "appointment_reminder_$appointmentId",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }



    // ---------- CATEGORY MAPPING ----------
    fun getCategoryIdByServiceName(serviceName: String): Int {
        return when (serviceName) {
            "Haircut" -> 1
            "Hair Wash" -> 2
            "Hair Coloring" -> 3
            "Hair Perm" -> 4
            else -> 0
        }
    }

    val timeSlots = timeSlotDao.getAllTimeSlotsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )
}
