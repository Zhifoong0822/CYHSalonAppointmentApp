package com.example.cyhsalonappointment.screens.Booking

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.cyhsalonappointment.AppointmentReminderReceiver
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
    fun scheduleNotification(context: Context, dateTime: LocalDateTime, appointmentId: String) {
        val reminderTime = dateTime.minusHours(1)

        val intent = Intent(context, AppointmentReminderReceiver::class.java).apply {
            putExtra("message", "Your appointment is in 1 hour.")
            putExtra("appointmentId", appointmentId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            appointmentId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            android.app.AlarmManager.RTC_WAKEUP,
            reminderTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli(),
            pendingIntent
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
