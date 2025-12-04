package com.example.cyhsalonappointment.screens.StylistSelection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cyhsalonappointment.local.DAO.StylistDao

class StylistSelectionViewModelFactory(
    private val dao: StylistDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StylistSelectionViewModel(dao) as T
    }
}
