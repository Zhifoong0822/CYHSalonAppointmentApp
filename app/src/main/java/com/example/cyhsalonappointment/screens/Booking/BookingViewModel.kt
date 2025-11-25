package com.example.cyhsalonappointment.screens.Booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyhsalonappointment.local.DAO.TimeSlotDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BookingViewModel(
    private val dao: TimeSlotDao
) : ViewModel() {

    val timeSlots = dao.getAllTimeSlotsFlow()
        .map { list -> list.map { it.timeSlot } }   // convert to List<String>
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )
}
