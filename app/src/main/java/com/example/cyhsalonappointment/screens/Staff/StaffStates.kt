package com.example.cyhsalonappointment.screens.Staff

//Staff Login state
data class StaffLoginState(
    val staffId: String = "",
    val password: String = "",
    val staff: StaffEntity? = null,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)