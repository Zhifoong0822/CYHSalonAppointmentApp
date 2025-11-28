package com.example.cyhsalonappointment.screens.Admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(private val repo: AdminRepository) : ViewModel() {

    private val _loginState = MutableStateFlow(false)
    val loginState: StateFlow<Boolean> = _loginState

    private val _currentAdmin = MutableStateFlow<AdminEntity?>(null)
    val currentAdmin: StateFlow<AdminEntity?> = _currentAdmin

    fun login(adminId: String, password: String) {
        viewModelScope.launch {
            val admin = repo.getAdmin(adminId, password)
            if (admin != null) {
                _currentAdmin.value = admin
                _loginState.value = true
            } else {
                _loginState.value = false
            }
        }
    }
}