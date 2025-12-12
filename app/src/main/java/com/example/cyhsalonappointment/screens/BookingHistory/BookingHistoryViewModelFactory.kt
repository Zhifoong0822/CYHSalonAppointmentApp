package com.example.cyhsalonappointment.screens.BookingHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cyhsalonappointment.local.DAO.AppointmentDao
import com.example.cyhsalonappointment.local.DAO.ServiceDao
import com.example.cyhsalonappointment.local.DAO.StylistDao

class BookingHistoryViewModelFactory(
    private val appointmentDao: AppointmentDao,
    private val serviceDao: ServiceDao,
    private val stylistDao: StylistDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingHistoryViewModel::class.java)) {
            return BookingHistoryViewModel(appointmentDao, serviceDao, stylistDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

