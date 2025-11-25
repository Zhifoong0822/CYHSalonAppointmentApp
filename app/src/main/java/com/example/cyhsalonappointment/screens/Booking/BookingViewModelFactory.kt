package com.example.cyhsalonappointment.screens.Booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cyhsalonappointment.local.DAO.TimeSlotDao

class BookingViewModelFactory(private val dao: TimeSlotDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookingViewModel(dao) as T
    }
}