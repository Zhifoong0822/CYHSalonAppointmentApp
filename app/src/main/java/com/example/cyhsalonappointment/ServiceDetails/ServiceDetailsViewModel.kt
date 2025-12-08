package com.example.cyhsalonappointment.ServiceDetails

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

    private val _service = MutableStateFlow<SalonService?>(null)
    val service: StateFlow<SalonService?> = _service

    fun loadService(serviceId: Int) {
        viewModelScope.launch {
            repo.getServiceById(serviceId).collect { service ->
                _service.value = service
            }
        }
    }
    fun getServicesByCategory(categoryId: Int): Flow<List<SalonService>> {
        return repo.getServicesByCategory(categoryId)
    }


    // selected price tier: short / medium / long / all
    private val _selectedOption = MutableStateFlow<String?>(null)
    val selectedOption: StateFlow<String?> = _selectedOption

    fun selectOption(option: String) {
        _selectedOption.value = option
    }

    fun getServiceById(id: Int): Flow<SalonService?> {
        return repo.getServiceById(id)
    }
}
