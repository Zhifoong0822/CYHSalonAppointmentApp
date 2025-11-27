package com.example.cyhsalonappointment.screens.Booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyhsalonappointment.local.DAO.AppointmentDao
import com.example.cyhsalonappointment.local.DAO.TimeSlotDao
import com.example.cyhsalonappointment.local.entity.Appointment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    fun createAppointment(date: String, timeSlotId: String, customerId: String?, serviceId: String?) {
        viewModelScope.launch {
            val newId = generateAppointmentId()

            val appointment = Appointment(
                appointmentId = newId,
                appointmentDate = date,
                timeSlotId = timeSlotId,
                customerId = customerId,
                serviceId = serviceId
            )

            appointmentDao.insertAppointment(appointment)
            _saveResult.value = true
        }
    }
    val timeSlots = timeSlotDao.getAllTimeSlotsFlow()
        .map { list -> list.map { it.timeSlot } }   // convert to List<String>
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )
}
