package com.example.cyhsalonappointment.screens.Admin

//Admin Login state
data class AdminLoginState(
    val adminId: String = "",
    val password: String = "",
    val admin: AdminEntity? = null,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)