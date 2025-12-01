package com.example.cyhsalonappointment.screens.Admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(private val repo: AdminRepository) : ViewModel() {

    private val _loginState = MutableStateFlow(AdminLoginState())
    val loginState: StateFlow<AdminLoginState> = _loginState

    private val _currentAdmin = MutableStateFlow<AdminEntity?>(null)
    val currentAdmin: StateFlow<AdminEntity?> = _currentAdmin

    fun onAdminIdChange(newValue: String) {
        _loginState.value = _loginState.value.copy(adminId = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _loginState.value = _loginState.value.copy(password = newValue)
    }

    fun login() {
        val adminId = _loginState.value.adminId
        val password = _loginState.value.password

        viewModelScope.launch {
            _loginState.value = _loginState.value.copy(isLoading = true, errorMessage = null)

            val admin = repo.login(adminId, password)

            if (admin != null) {
                _currentAdmin.value = admin
                _loginState.value = _loginState.value.copy(
                    isLoading = false,
                    isSuccess = true
                )
            } else {
                _loginState.value = _loginState.value.copy(
                    isLoading = false,
                    isSuccess = false,
                    errorMessage = "Invalid admin ID or password"
                )
            }
        }
    }

    fun clearError() {
        _loginState.value = _loginState.value.copy(errorMessage = null)
    }
}