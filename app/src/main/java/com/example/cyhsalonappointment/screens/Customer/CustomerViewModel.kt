package com.example.cyhsalonappointment.screens.Customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CustomerViewModel(private val repository: CustomerRepository) : ViewModel() {

    // Holds the logged-in customer
    private val _loggedInCustomer = MutableStateFlow<CustomerEntity?>(null)
    val loggedInCustomer: StateFlow<CustomerEntity?> = _loggedInCustomer

    // Holds registration status (success / failure)
    private val _registrationResult = MutableStateFlow<Boolean?>(null)
    val registrationResult: StateFlow<Boolean?> = _registrationResult

    // Login function
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val customer = repository.loginCustomer(email, password)
            _loggedInCustomer.value = customer
        }
    }

    // Register function
    fun register(customer: CustomerEntity) {
        viewModelScope.launch {
            try {
                repository.registerCustomer(customer)
                _registrationResult.value = true
            } catch (e: Exception) {
                _registrationResult.value = false
            }
        }
    }

    // Optional: clear login state
    fun logout() {
        _loggedInCustomer.value = null
    }
}