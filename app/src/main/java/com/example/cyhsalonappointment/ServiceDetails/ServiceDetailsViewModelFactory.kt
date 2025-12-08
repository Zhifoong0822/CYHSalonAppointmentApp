package com.example.cyhsalonappointment.ServiceDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cyhsalonappointment.data.ServiceRepository

class ServiceDetailViewModelFactory(
    private val repository: ServiceRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ServiceDetailViewModel(repository) as T
    }
}
