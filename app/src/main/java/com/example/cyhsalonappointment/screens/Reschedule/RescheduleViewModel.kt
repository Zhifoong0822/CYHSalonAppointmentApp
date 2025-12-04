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

    // Load all time slots as List<String>
    val timeSlots = timeSlotDao.getAllTimeSlotsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    // Update appointment date + timeslot
    fun updateAppointment(
        appointmentId: String,
        newDate: String,
        newTimeSlotId: String
    ) {
        viewModelScope.launch {
            appointmentDao.updateAppointment(
                appointmentId = appointmentId,
                newDate = newDate,
                newTimeSlotId = newTimeSlotId
            )
        }
    }
}
