package com.example.cyhsalonappointment.screens.BookingHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cyhsalonappointment.local.DAO.AppointmentDao

class BookingHistoryViewModelFactory(private val dao: AppointmentDao)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookingHistoryViewModel(dao) as T
    }
}
