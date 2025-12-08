package com.example.cyhsalonappointment.screens.StylistSelection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cyhsalonappointment.data.ServiceRepository
import com.example.cyhsalonappointment.local.DAO.StylistDao

class StylistSelectionViewModelFactory(
    private val stylistDao: StylistDao,
    private val serviceRepo: ServiceRepository
) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return StylistSelectionViewModel(stylistDao, serviceRepo) as T
    }
}

