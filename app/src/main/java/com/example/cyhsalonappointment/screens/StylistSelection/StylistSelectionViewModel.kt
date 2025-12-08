package com.example.cyhsalonappointment.screens.StylistSelection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyhsalonappointment.data.ServiceRepository
import com.example.cyhsalonappointment.local.DAO.StylistDao
import com.example.cyhsalonappointment.local.entity.Stylist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StylistSelectionViewModel(
    private val stylistDao: StylistDao,
    private val serviceRepo: ServiceRepository
) : ViewModel() {

    private val _stylists = MutableStateFlow<List<Stylist>>(emptyList())
    val stylists = _stylists.asStateFlow()

    var selectedStylistId by mutableStateOf<String?>(null)

    init {
        viewModelScope.launch {
            _stylists.value = stylistDao.getAllStylists()
        }
    }

    fun select(stylistId: String) {
        selectedStylistId = stylistId
    }

    // load service by ID from DB
    fun getService(serviceId: Int) = serviceRepo.getServiceById(serviceId)

    fun getStylistPriceMultiplier(level: String): Double {
        return when (level.lowercase()) {
            "junior" -> 1.0
            "senior" -> 1.2
            "director" -> 1.5
            else -> 1.0
        }
    }
    fun calculateFinalPrice(basePrice: Double, stylistLevel: String): Double {
        val multiplier = getStylistPriceMultiplier(stylistLevel)
        return basePrice * multiplier
    }

}
