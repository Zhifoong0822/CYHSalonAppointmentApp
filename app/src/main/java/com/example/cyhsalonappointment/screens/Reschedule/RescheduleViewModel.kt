package com.example.cyhsalonappointment.screens.Reschedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyhsalonappointment.local.DAO.AppointmentDao
import com.example.cyhsalonappointment.local.DAO.TimeSlotDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RescheduleViewModel(
    private val timeSlotDao: TimeSlotDao,
    private val appointmentDao: AppointmentDao
) : ViewModel() {

    private val rescheduledAppointments = mutableSetOf<String>()

    // Load all time slots as List<String>
    val timeSlots = timeSlotDao.getAllTimeSlotsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    fun isAlreadyRescheduled(appointmentId: String) = rescheduledAppointments.contains(appointmentId)

    // Update appointment date + timeslot
    fun updateAppointment(
        appointmentId: String,
        newDate: String,
        newTimeSlotId: String,
        oldDate: String,
        oldTimeSlotId: String,
        onError: (String) -> Unit = {},
        onSuccess: () -> Unit = {}
    ){
        viewModelScope.launch {

            if (newDate == oldDate && newTimeSlotId == oldTimeSlotId) {
                onError("Please select a different date or time slot.")
                return@launch
            }

            if (rescheduledAppointments.contains(appointmentId)) {
                onError("You can only reschedule once per session.")
                return@launch
            }

            // Update the appointment
            appointmentDao.updateAppointment(
                appointmentId = appointmentId,
                newDate = newDate,
                newTimeSlotId = newTimeSlotId
            )

            // Mark as rescheduled in memory
            rescheduledAppointments.add(appointmentId)

            onSuccess()
        }
    }
}
