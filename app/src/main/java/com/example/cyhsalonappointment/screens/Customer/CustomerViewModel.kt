package com.example.cyhsalonappointment.screens.Customer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyhsalonappointment.local.datastore.UserSessionManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

data class SignUpState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val contactNumber: String = "",
    val gender: String = "",
    val birthdate: String = "",

    // Username checking
    val isCheckingUsername: Boolean = false,
    val isUsernameAvailable: Boolean? = null,
    val usernameError: String? = null,

    // Form validation errors
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val contactError: String? = null,
    val birthdateError: String? = null,
    val genderError: String? = null,

    // Result UI
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

data class UiState(
    val userProfile: CustomerEntity? = null,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val isAuthChecking: Boolean = true,
    val errorMessage: String? = null
)

data class EditProfileState(
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class CustomerViewModel(private val repository: CustomerRepository,
                        private val dataStoreManager: UserSessionManager
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    private var usernameCheckJob: Job? = null

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> = _signUpState

    private val _uiState = MutableStateFlow(UiState(isLoading = true))
    val uiState: StateFlow<UiState> = _uiState

    private val _customerProfile = MutableStateFlow<CustomerEntity?>(null)
    val customerProfile = _customerProfile

    private val _editProfileState = MutableStateFlow(EditProfileState())
    val editProfileState = _editProfileState.asStateFlow()

    // Logged-in user (optional)
    private val _loggedInCustomer = MutableStateFlow<CustomerEntity?>(null)
    val loggedInCustomer: StateFlow<CustomerEntity?> = _loggedInCustomer

    // For controlling form clear
    private val _shouldClearLoginForm = MutableStateFlow(false)
    val shouldClearLoginForm: StateFlow<Boolean> = _shouldClearLoginForm

    private val _shouldClearSignUpForm = MutableStateFlow(false)
    val shouldClearSignUpForm: StateFlow<Boolean> = _shouldClearSignUpForm

    // Registration result
    private val _registrationResult = MutableStateFlow<Boolean?>(null)
    val registrationResult: StateFlow<Boolean?> = _registrationResult

    // LOGIN
    fun login() {
        viewModelScope.launch {
            _loginState.value = _loginState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            val email = _loginState.value.email
            val password = _loginState.value.password

            val customer = repository.loginCustomer(email, password)

            if (customer != null) {
                // Login succeeded
                _loggedInCustomer.value = customer
                _uiState.value = UiState(userProfile = customer)

                launch { dataStoreManager.saveUserEmail(customer.email) }
                launch { dataStoreManager.saveLoginStatus(true) }
                launch { dataStoreManager.saveUserId(customer.customerId) }

                _loginState.value = _loginState.value.copy(
                    isLoading = false,
                    successMessage = "Login successful",
                    errorMessage = null
                )

                _shouldClearLoginForm.value = true

            } else {
                // Login failed
                _loginState.value = _loginState.value.copy(
                    isLoading = false,
                    errorMessage = "Invalid email or password",
                    successMessage = null
                )
            }
        }
    }

    // SIGNUP
    fun sign_up(customer: CustomerEntity) {
        viewModelScope.launch {
            // set loading true
            _signUpState.value = _signUpState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            val result = repository.signUp(customer)

            when (result) {
                is AuthResult.Success -> {
                    _registrationResult.value = true
                    _loggedInCustomer.value = result.data
                    dataStoreManager.saveUserEmail(result.data.email)
                    _signUpState.value = _signUpState.value.copy(
                        isLoading = false,
                        successMessage = "Sign up successful!"
                    )
                }
                is AuthResult.Error -> {
                    _registrationResult.value = false
                    _signUpState.value = _signUpState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message
                    )
                }
            }
        }
    }

    fun onLoginEmailChange(newEmail: String) {
        _loginState.value = _loginState.value.copy(email = newEmail)
    }

    fun onLoginPasswordChange(newPassword: String) {
        _loginState.value = _loginState.value.copy(password = newPassword)
    }

    fun onSignUpUsernameChange(username: String) {
        _signUpState.value = _signUpState.value.copy(
            username = username,
            usernameError = null,
            successMessage = null,
            errorMessage = null
        )

        if (username.length >= 2) {
            usernameCheckJob?.cancel()
            usernameCheckJob = viewModelScope.launch {
                delay(500) // debounce
                checkUsernameAvailability(username)
            }
        }
    }

    private fun checkUsernameAvailability(username: String) {
        viewModelScope.launch {
            try {
                _signUpState.value = _signUpState.value.copy(isCheckingUsername = true)

                val available = repository.isUsernameAvailable(username)

                _signUpState.value = _signUpState.value.copy(
                    isCheckingUsername = false,
                    isUsernameAvailable = available,
                    usernameError = if (!available) "Username already taken" else null
                )
            } catch (e: Exception) {
                _signUpState.value = _signUpState.value.copy(
                    isCheckingUsername = false,
                    usernameError = "Error checking username"
                )
            }
        }
    }

    fun setConfirmPasswordError(message: String?) {
        _signUpState.value = _signUpState.value.copy(confirmPasswordError = message)
    }

    fun onSignUpBirthdateChange(birthdate: String) {
        _signUpState.value = _signUpState.value.copy(
            birthdate = birthdate,
            errorMessage = null
        )
    }

    fun onSignUpGenderChange(gender: String) {
        _signUpState.value = _signUpState.value.copy(
            gender = gender,
            errorMessage = null
        )
    }

    fun onSignUpContactChange(contact: String) {
        _signUpState.value = _signUpState.value.copy(
            contactNumber = contact,
            contactError = null
        )
    }

    fun onSignUpEmailChange(email: String) {
        _signUpState.value = _signUpState.value.copy(
            email = email,
            emailError = null
        )
    }

    fun onSignUpPasswordChange(password: String) {
        _signUpState.value = _signUpState.value.copy(
            password = password,
            passwordError = null,
            confirmPasswordError = null
        )
    }

    fun onSignUpConfirmPasswordChange(confirm: String) {
        _signUpState.value = _signUpState.value.copy(
            confirmPassword = confirm,
            confirmPasswordError = null
        )
    }

    fun loadLocalUserProfile(email: String) {
        viewModelScope.launch {
            Log.d("ProfileScreen", "Loading profile for ID: $email")

            val localUser = repository.loadCustomerProfile(email)

            Log.d("ProfileScreen", "User loaded: $localUser")

            if (localUser != null) {
                // Save session info in DataStore
                dataStoreManager.saveLoginStatus(true)
                dataStoreManager.saveUserId(localUser.customerId)

                _uiState.value = _uiState.value.copy(
                    userProfile = localUser, // directly use CustomerEntity
                    isLoggedIn = true,
                    isLoading = false,
                    isAuthChecking = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAuthChecking = false,
                    errorMessage = "No local profile found"
                )
            }
        }
    }

    fun updateProfile(updatedCustomer: CustomerEntity) {
        viewModelScope.launch {
            val success = repository.updateCustomer(updatedCustomer)
            if (success) {
                // Update UI state
                _uiState.value = _uiState.value.copy(
                    userProfile = updatedCustomer,
                    errorMessage = null
                )
                // Set success message for Snackbar
                _editProfileState.value = _editProfileState.value.copy(
                    successMessage = "Profile updated successfully"
                )
            } else {
                _editProfileState.value = _editProfileState.value.copy(
                    errorMessage = "Failed to update profile"
                )
            }
        }
    }

    // Clear success/error messages after showing Snackbar
    fun clearEditProfileSuccessMessage() {
        _editProfileState.value = _editProfileState.value.copy(successMessage = null)
    }

    fun clearEditProfileErrorMessage() {
        _editProfileState.value = _editProfileState.value.copy(errorMessage = null)
    }

    // Optional: reset form
    fun clearEditProfileForm() {
        _editProfileState.value = EditProfileState()
    }

    fun deleteAccount(userId: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                // Validate user
                val user = repository.loginCustomer(email, password)

                if (user == null || user.customerId != userId) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Incorrect password. Unable to delete account."
                    )
                    return@launch
                }

                // Delete account
                repository.deleteCustomer(userId)

                // Clear ViewModel logout state
                _loggedInCustomer.value = null
                _uiState.value = UiState(
                    userProfile = null,
                    isLoggedIn = false,
                    errorMessage = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error deleting account."
                )
            }
        }
    }

    // LOG OUT
    fun logout() {
        viewModelScope.launch {
            dataStoreManager.clearSession()   // Clears userId + login status
            _loggedInCustomer.value = null
            _uiState.value = UiState()
            clearLoginForm()
            clearSignUpForm()
        }
    }

    fun clearLoginForm() {
        _loginState.value = LoginState()
        _shouldClearLoginForm.value = false
    }

    fun clearSignUpForm() {
        _signUpState.value = SignUpState()
        _shouldClearSignUpForm.value = false
    }

    fun forceCleanState() {
        _loggedInCustomer.value = null
        _registrationResult.value = null
    }

    //Clears login or registration messages after showing snackbar
    fun clearLoginMessages() {
        _registrationResult.value = null
    }

    fun clearSignUpMessages() {
        _signUpState.value = _signUpState.value.copy(
            successMessage = null,
            errorMessage = null
        )
    }

    suspend fun getStoredUserId(): String? {
        return dataStoreManager.getUserId().first()
    }
}