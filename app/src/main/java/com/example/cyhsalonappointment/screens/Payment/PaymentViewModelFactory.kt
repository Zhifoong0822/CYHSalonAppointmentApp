
package com.example.cyhsalonappointment.screens.Payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cyhsalonappointment.local.DAO.AppointmentDao
import com.example.cyhsalonappointment.local.DAO.PaymentDao

class PaymentViewModelFactory(
    private val paymentDao: PaymentDao,
    private val appointmentDao: AppointmentDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            return PaymentViewModel(paymentDao,appointmentDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
