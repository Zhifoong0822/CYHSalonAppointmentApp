package com.example.cyhsalonappointment.screens.Customer

//General Customer authentication state
data class CustomerAuthState(
    val email: String = "",
    val password: String = "",
    val isLoggedIn: Boolean = false,
    val customer: CustomerEntity? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

//Customer Login form state
data class CustomerLoginState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

//Customer Sign-Up form state
data class CustomerSignUpState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val username: String = "",
    val gender: String = "",
    val contactNumber: String = "",
    val role: String = "customer",  // fixed role
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val usernameError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

//Customer Reset Password state
data class ResetPasswordState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

//Customer Edit Profile state
data class EditCustomerProfileState(
    val newUsername: String = "",
    val newGender: String = "",
    val newContactNumber: String = "",
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)