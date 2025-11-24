package com.example.cyhsalonappointment.screens.Staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StaffViewModel(private val repo: StaffRepository) : ViewModel() {

    private val _loginState = MutableStateFlow(false)
    val loginState: StateFlow<Boolean> = _loginState

    private val _currentStaff = MutableStateFlow<StaffEntity?>(null)
    val currentStaff: StateFlow<StaffEntity?> = _currentStaff

    fun login(staffId: String, password: String) {
        viewModelScope.launch {
            val staff = repo.getStaff(staffId, password)
            if (staff != null) {
                _currentStaff.value = staff
                _loginState.value = true
            } else {
                _loginState.value = false
            }
        }
    }
}