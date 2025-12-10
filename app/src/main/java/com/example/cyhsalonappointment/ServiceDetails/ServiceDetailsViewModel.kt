package com.example.cyhsalonappointment.ServiceDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyhsalonappointment.data.ServiceRepository
import com.example.cyhsalonappointment.local.entity.SalonService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ServiceDetailViewModel(
    private val repo: ServiceRepository
) : ViewModel() {

    // --- Selected service ---
    private val _selectedService = MutableStateFlow<SalonService?>(null)
    val selectedService: StateFlow<SalonService?> = _selectedService

    // --- List of services for a category ---
    private val _services = MutableStateFlow<List<SalonService>>(emptyList())
    val services: StateFlow<List<SalonService>> = _services

    // --- Selected price option ---
    private val _selectedOption = MutableStateFlow<String?>(null)
    val selectedOption: StateFlow<String?> = _selectedOption

    // --- Load services for a category ---
    fun loadServicesByCategory(categoryId: Int) {
        viewModelScope.launch {
            repo.getServicesByCategory(categoryId).collect { list ->
                Log.d("VM", "Loaded services for category $categoryId: $list")
                _services.value = list
            }
        }
    }

    // --- Load a single service by ID ---
    fun loadService(serviceId: Int) {
        viewModelScope.launch {
            repo.getServiceById(serviceId).collect { service ->
                _selectedService.value = service
            }
        }
    }

    // --- Set selected price tier ---
    fun selectOption(option: String) {
        _selectedOption.value = option
    }

    // --- Optional: get a service as Flow directly ---
    fun getServiceById(id: Int): Flow<SalonService?> {
        return repo.getServiceById(id)
    }
    fun getServicesByCategory(categoryId: Int): Flow<List<SalonService>> {
        return repo.getServicesByCategory(categoryId)
    }




}

