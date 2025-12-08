package com.example.cyhsalonappointment.screens.Booking

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.cyhsalonappointment.AppointmentReminderWorker
import com.example.cyhsalonappointment.local.DAO.AppointmentDao
import com.example.cyhsalonappointment.local.DAO.TimeSlotDao
import com.example.cyhsalonappointment.local.entity.Appointment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class BookingViewModel(
    private val appointmentDao: AppointmentDao,
    private val timeSlotDao: TimeSlotDao
) : ViewModel() {

    // Live result feedback
    private val _saveResult = MutableStateFlow<Boolean?>(null)
    val saveResult = _saveResult

    // Generate ID like APP0001
    private suspend fun generateAppointmentId(): String {
        val count = appointmentDao.getAppointmentCount()  // returns Int
        val nextId = count + 1
        return "APP" + String.format("%04d", nextId)
    }

    fun getCategoryIdByServiceName(serviceName: String): Int {
        return when(serviceName) {
            "Haircut" -> 1
            "Hair Wash" -> 2
            "Hair Coloring" -> 3
            "Hair Perm" -> 4
            else -> 0 // fallback or error
        }
    }

    fun createAppointment(date: String, timeSlotId: String, customerId: String?, serviceId: String?, stylistId: String?) {
        viewModelScope.launch {
            val newId = generateAppointmentId()

            val appointment = Appointment(
                appointmentId = newId,
                appointmentDate = date,
                timeSlotId = timeSlotId,
                customerId = customerId,
                serviceId = serviceId,
                stylistId = stylistId
            )

            appointmentDao.insertAppointment(appointment)
            _saveResult.value = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleNotification(context: Context, dateTime: LocalDateTime) {

        val reminderTime = dateTime.minusHours(1)  // 1 hour before

        val delay = Duration.between(
            LocalDateTime.now(),
            reminderTime
        ).toMillis()

        if (delay <= 0) return // skip if the time already passed

        val data = workDataOf(
            "message" to "Your appointment is in 1 hour."
        )

        val request = OneTimeWorkRequestBuilder<AppointmentReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }

    val timeSlots = timeSlotDao.getAllTimeSlotsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )


}
