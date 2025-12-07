package com.example.cyhsalonappointment.screens.Admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyhsalonappointment.data.ServiceRepository
import com.example.cyhsalonappointment.local.entity.SalonService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiceViewModel(
    private val repository: ServiceRepository
) : ViewModel() {

    // Live categories from DB (if needed later)
    val categories = repository.getCategories().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    // Only ACTIVE services come from repository (because DAO filters isActive = 1)
    val services = repository.getAllServices().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    // Fixed category names used by AddServiceScreen
    val fixedCategories = listOf(
        "Hair Cut",
        "Hair Wash",
        "Hair Colouring",
        "Hair Perm"
    )

    // Helper: get category name from id for Edit screen
    fun getCategoryNameById(categoryId: Int): String {
        return when (categoryId) {
            1 -> "Hair Cut"
            2 -> "Hair Wash"
            3 -> "Hair Colouring"
            4 -> "Hair Perm"
            else -> "Unknown"
        }
    }

    // Ensure category exists in DB and return its id
    private suspend fun ensureCategoryId(name: String): Int {
        val existing = repository.getCategoryByName(name)
        if (existing != null) return existing.categoryId

        // create new category
        repository.addCategory(name)
        return repository.getCategoryByName(name)!!.categoryId
    }

    // --------- CREATE SERVICES ---------

    // One price for all lengths
    fun addSinglePriceService(category: String, name: String, price: String) {
        viewModelScope.launch {
            val id = ensureCategoryId(category)
            repository.addService(
                SalonService(
                    categoryId = id,
                    serviceName = name,
                    priceAll = price.toDouble(),
                    priceShort = null,
                    priceMedium = null,
                    priceLong = null
                )
            )
        }
    }

    // Different price per length
    fun addLengthPriceService(
        category: String,
        name: String,
        short: String,
        medium: String,
        long: String
    ) {
        viewModelScope.launch {
            val id = ensureCategoryId(category)
            repository.addService(
                SalonService(
                    categoryId = id,
                    serviceName = name,
                    priceAll = null,
                    priceShort = short.toDouble(),
                    priceMedium = medium.toDouble(),
                    priceLong = long.toDouble()
                )
            )
        }
    }

    // --------- UPDATE & HIDE ---------



    fun updateService(service: SalonService) = viewModelScope.launch {
        repository.updateService(service)
    }

    // Hide service (soft delete)
    fun hideService(service: SalonService, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.softDelete(service.serviceId)
            onDone()
        }
    }
}
