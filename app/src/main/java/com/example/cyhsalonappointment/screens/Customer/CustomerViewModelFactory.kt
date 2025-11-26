package com.example.cyhsalonappointment.screens.Customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cyhsalonappointment.local.datastore.UserSessionManager

class CustomerViewModelFactory(
    private val repository: CustomerRepository,
    private val dataStoreManager: UserSessionManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CustomerViewModel(repository,dataStoreManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}