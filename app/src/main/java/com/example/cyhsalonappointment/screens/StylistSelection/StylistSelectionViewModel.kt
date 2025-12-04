package com.example.cyhsalonappointment.screens.StylistSelection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyhsalonappointment.local.DAO.StylistDao
import com.example.cyhsalonappointment.local.entity.Stylist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StylistSelectionViewModel(
    private val dao: StylistDao
) : ViewModel() {

    private val _stylists = MutableStateFlow<List<Stylist>>(emptyList())
    val stylists = _stylists.asStateFlow()

    var selectedStylistId by mutableStateOf<String?>(null)

    init {
        viewModelScope.launch {
            _stylists.value = dao.getAllStylists()
        }
    }

    fun select(stylistId: String) {
        selectedStylistId = stylistId
    }
}
