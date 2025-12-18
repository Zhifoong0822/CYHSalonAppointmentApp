package com.example.cyhsalonappointment.screens.Admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyhsalonappointment.data.ServiceRepository
import com.example.cyhsalonappointment.local.entity.SalonService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ServiceViewModel(
    private val repository: ServiceRepository
) : ViewModel() {

    // Active services
    val services = repository.getAllServices()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    // Error message for UI feedback (Toast / Snackbar)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // --------- CREATE SERVICES ---------

    // Single price services (e.g. haircut, touch up)
    fun addSinglePriceService(
        categoryId: Int,
        name: String,
        price: String
    ) {
        viewModelScope.launch {

            // 🔒 DUPLICATE CHECK
            if (repository.serviceExists(name, categoryId)) {
                _errorMessage.value = "Service already exists"
                return@launch
            }

            repository.addService(
                SalonService(
                    categoryId = categoryId,
                    serviceName = name,
                    priceAll = price.toDouble(),
                    priceShort = null,
                    priceMedium = null,
                    priceLong = null,
                    isActive = true
                )
            )
        }
    }

    // Length-based price services (e.g. colour, perm)
    fun addLengthPriceService(
        categoryId: Int,
        name: String,
        short: String,
        medium: String,
        long: String
    ) {
        viewModelScope.launch {

            // 🔒 DUPLICATE CHECK
            if (repository.serviceExists(name, categoryId)) {
                _errorMessage.value = "Service already exists"
                return@launch
            }

            repository.addService(
                SalonService(
                    categoryId = categoryId,
                    serviceName = name,
                    priceAll = null,
                    priceShort = short.toDouble(),
                    priceMedium = medium.toDouble(),
                    priceLong = long.toDouble(),
                    isActive = true
                )
            )
        }
    }

    // --------- UPDATE & HIDE ---------

    fun updateService(service: SalonService) {
        viewModelScope.launch {
            repository.updateService(service)
        }
    }

    fun hideService(service: SalonService, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.softDelete(service.serviceId)
            onDone()
        }
    }

    // Fixed category name helper (used by Edit screen)
    fun getCategoryNameById(categoryId: Int): String {
        return when (categoryId) {
            1 -> "Hair Cut"
            2 -> "Hair Wash"
            3 -> "Hair Colouring"
            4 -> "Hair Perm"
            else -> "Unknown"
        }
    }

    // Call this after showing error to clear state
    fun clearError() {
        _errorMessage.value = null
    }
}
