package com.example.cyhsalonappointment.screens.Reschedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cyhsalonappointment.local.DAO.AppointmentDao
import com.example.cyhsalonappointment.local.DAO.TimeSlotDao

class RescheduleViewModelFactory(
    private val timeSlotDao: TimeSlotDao,
    private val appointmentDao: AppointmentDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RescheduleViewModel::class.java)) {
            return RescheduleViewModel(timeSlotDao, appointmentDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}