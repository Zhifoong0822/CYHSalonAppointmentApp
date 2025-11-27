package com.example.cyhsalonappointment.screens.Booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cyhsalonappointment.local.DAO.AppointmentDao
import com.example.cyhsalonappointment.local.DAO.TimeSlotDao

class BookingViewModelFactory(
    private val timeSlotDao: TimeSlotDao,
    private val appointmentDao: AppointmentDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookingViewModel(appointmentDao, timeSlotDao) as T
    }
}
