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
    val newUsername: String = "",
    val newGender: String = "",
    val newEmail: String = "",
    val newContactNumber: String = "",

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

data class UserProfile(
    val customerId: String,
    val username: String,
    val gender: String,
    val email: String,
    val contactNumber: String,
    val createdAt: Long,
    val updatedAt: Long
)

data class ResetPasswordState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

data class DeleteAccountState(
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
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

    private val _resetPasswordState = MutableStateFlow(ResetPasswordState())
    val resetPasswordState = _resetPasswordState

    private val _deleteAccountState = MutableStateFlow(DeleteAccountState())
    val deleteAccountState = _deleteAccountState.asStateFlow()

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

    init {
        checkLoggedInUser()
    }

    fun checkLoggedInUser() {
        viewModelScope.launch {
            val loggedIn = dataStoreManager.isLoggedIn().first()
            val email = if (loggedIn) dataStoreManager.getUserEmail().first() else ""

            if (loggedIn && email.isNotEmpty()) {
                // Load the profile; it will handle updating _uiState itself
                loadLocalUserProfile(email)
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoggedIn = false,
                    isLoading = false,
                    isAuthChecking = false
                )
            }
        }
    }

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
                _uiState.value = _uiState.value.copy(
                    userProfile = customer,
                    isLoggedIn = true,
                    isLoading = false,
                    isAuthChecking = false,
                    errorMessage = null
                )

                dataStoreManager.saveUserEmail(customer.email)
                dataStoreManager.saveLoginStatus(true)
                dataStoreManager.saveUserId(customer.customerId)

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
            // Validate username
            val usernameError = validateUsername(signUpState.value.username)
            if (usernameError != null) {
                _signUpState.value = _signUpState.value.copy(
                    usernameError = usernameError,
                    errorMessage = "Please fix the errors before submitting"
                )
                return@launch
            }

            // Validate gender
            val genderError = validateGender(signUpState.value.gender)
            if (genderError != null) {
                _signUpState.value = _signUpState.value.copy(
                    genderError = genderError,
                    errorMessage = "Please fix the errors before submitting"
                )
                return@launch
            }

            // Validate email
            val emailError = validateEmail(signUpState.value.email)
            if (emailError != null) {
                _signUpState.value = _signUpState.value.copy(
                    emailError = emailError,
                    errorMessage = "Please fix the errors before submitting"
                )
                return@launch
            }

            // Validate contact number
            val contactError = validateContactNumber(signUpState.value.contactNumber)
            if (contactError != null) {
                _signUpState.value = _signUpState.value.copy(
                    contactError = contactError,
                    errorMessage = "Please fix the errors before submitting"
                )
                return@launch
            }

            // Validate password
            val passwordError = validatePassword(signUpState.value.password)
            if (passwordError != null) {
                _signUpState.value = _signUpState.value.copy(
                    passwordError = passwordError,
                    errorMessage = "Please fix the errors before submitting"
                )
                return@launch
            }

            _signUpState.value = _signUpState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            try {
                // Step 1: Get existing IDs
                val existingIds = repository.getAllCustomerIds()
                val newId = generateCustomerId(existingIds)

                // Step 2: Build new CustomerEntity
                val newCustomer = CustomerEntity(
                    customerId = newId,
                    username = signUpState.value.username.trim(),
                    email = signUpState.value.email.trim(),
                    password = signUpState.value.password,
                    gender = signUpState.value.gender,
                    contactNumber = signUpState.value.contactNumber,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                // Step 3: Insert customer through repository
                val result = repository.signUp(newCustomer)

                // Step 4: Handle result
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

            } catch (e: Exception) {
                _signUpState.value = _signUpState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error during signup"
                )
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
        val usernameError = validateUsername(username)

        _signUpState.value = _signUpState.value.copy(
            username = username,
            usernameError = usernameError,
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

    fun generateCustomerId(existingIds: List<String>): String {
        if (existingIds.isEmpty()) return "C0001" // first customer

        val maxNumber = existingIds
            .map { it.removePrefix("C").toIntOrNull() ?: 0 } // remove "C" and convert
            .maxOrNull() ?: 0

        val nextNumber = maxNumber + 1
        return "C" + nextNumber.toString().padStart(4, '0')
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

    fun onSignUpGenderChange(gender: String) {
        _signUpState.value = _signUpState.value.copy(
            gender = gender,
            genderError = null,
            errorMessage = null
        )
    }

    fun onSignUpContactChange(contact: String) {
        // Count digits only (excluding dashes, spaces, etc.)
        val digitsOnly = contact.filter { it.isDigit() }

        // Prevent entering more than 11 digits
        if (digitsOnly.length > 11) {
            return
        }

        val contactError = validateContactNumber(contact)

        _signUpState.value = _signUpState.value.copy(
            contactNumber = contact,
            contactError = contactError,
            errorMessage = null
        )
    }

    fun onSignUpEmailChange(email: String) {
        val emailError = validateEmail(email)

        _signUpState.value = _signUpState.value.copy(
            email = email,
            emailError = emailError
        )
    }

    fun onSignUpPasswordChange(password: String) {
        val passwordError = validatePassword(password)

        _signUpState.value = _signUpState.value.copy(
            password = password,
            passwordError = passwordError,
            confirmPasswordError = null
        )
    }

    fun onSignUpConfirmPasswordChange(confirm: String) {
        _signUpState.value = _signUpState.value.copy(
            confirmPassword = confirm,
            confirmPasswordError = null
        )
    }

    private fun validateUsername(username: String): String? {
        return when {
            username.isBlank() -> "Username cannot be empty"
            username.any { it.isDigit() } -> "Username cannot contain numbers"
            !username.all { it.isLetter() || it.isWhitespace() } -> "Username can only contain letters"
            else -> null // Valid username
        }
    }

    private fun validateGender(gender: String): String? {
        return when {
            gender.isBlank() -> "Please select a gender"
            else -> null
        }
    }

    private fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email cannot be empty"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email format"
            !email.endsWith("@gmail.com", ignoreCase = true) &&
                    !email.endsWith("@yahoo.com", ignoreCase = true) -> "Invalid email format"
            else -> null // Email is valid
        }
    }

    private fun validateContactNumber(contactNumber: String): String? {
        // Remove all non-digit characters (dashes, spaces, etc.) to count only digits
        val digitsOnly = contactNumber.filter { it.isDigit() }

        return when {
            contactNumber.isBlank() -> "Contact number cannot be empty"
            digitsOnly.length < 10 -> "Contact number must have at least 10 digits"
            digitsOnly.length > 11 -> "Contact number cannot exceed 11 digits"
            else -> null // Valid contact number
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Password cannot be empty"
            password.length < 8 -> "Password must be at least 8 characters"
            !password.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter"
            !password.any { it.isDigit() } -> "Password must contain at least one number"
            !password.any { !it.isLetterOrDigit() } -> "Password must contain at least one special character"
            else -> null // Password is valid
        }
    } //atleast one capital letter, atleast one digit, atleast one special character, atleast 8 characters

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
                    isAuthChecking = false,
                    errorMessage = null
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    userProfile = null,
                    isLoggedIn = false,
                    isLoading = false,
                    isAuthChecking = false,
                    errorMessage = "No local profile found"
                )
            }
        }
    }

    fun updateUserProfile(updated: UserProfile) {
        viewModelScope.launch {
            _editProfileState.value = _editProfileState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            try {
                repository.updateCustomerProfile(
                    customerId = updated.customerId,
                    username = updated.username,
                    gender = updated.gender,
                    email = updated.email,
                    contactNumber = updated.contactNumber
                )

                // update UI (refresh profile)
                _uiState.value = _uiState.value.copy(
                    userProfile = CustomerEntity(
                        customerId = updated.customerId,
                        username = updated.username,
                        gender = updated.gender,
                        email = updated.email,
                        contactNumber = updated.contactNumber,
                        password = _uiState.value.userProfile?.password ?: "", // keep old password
                        createdAt = updated.createdAt,
                        updatedAt = updated.updatedAt
                    )
                )

                _editProfileState.value = _editProfileState.value.copy(
                    isLoading = false,
                    successMessage = "Profile updated"
                )

            } catch (e: Exception) {
                _editProfileState.value = _editProfileState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun updateNewUsername(value: String) {
        _editProfileState.value = _editProfileState.value.copy(newUsername = value)
    }

    fun updateNewGender(value: String) {
        _editProfileState.value = _editProfileState.value.copy(newGender = value)
    }

    fun updateNewContactNumber(value: String) {
        _editProfileState.value = _editProfileState.value.copy(newContactNumber = value)
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _resetPasswordState.value = ResetPasswordState(isLoading = true)

            val customer = repository.getCustomerByEmail(email)

            if (customer == null) {
                _resetPasswordState.value = ResetPasswordState(
                    isLoading = false,
                    errorMessage = "Email not found."
                )
                return@launch
            }

            // Simulate sending an email
            delay(1500)

            _resetPasswordState.value = ResetPasswordState(
                isLoading = false,
                successMessage = "Password reset link has been sent to your email."
            )
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

    fun clearResetPasswordMessages() {
        _resetPasswordState.value = ResetPasswordState()
    }

    fun deleteAccount(userId: String, email: String, password: String) {
        viewModelScope.launch {
            if (password.isBlank()) {
                _deleteAccountState.value = DeleteAccountState(
                    isDeleting = false,
                    errorMessage = "Please enter your password"
                )
                return@launch
            }

            _deleteAccountState.value = DeleteAccountState(isDeleting = true)

            try {
                // Validate user
                val user = repository.loginCustomer(email, password)

                if (user == null) {
                    _deleteAccountState.value = DeleteAccountState(
                        isDeleting = false,
                        errorMessage = "Incorrect password. Please try again."
                    )
                    return@launch
                }

                if (user.customerId != userId) {
                    _deleteAccountState.value = DeleteAccountState(
                        isDeleting = false,
                        errorMessage = "Authentication failed"
                    )
                    return@launch
                }

                // Delete account
                repository.deleteCustomer(userId)

                // Clear DataStore session
                dataStoreManager.clearSession()

                // Clear in-memory state
                _loggedInCustomer.value = null
                _uiState.value = UiState()  // Reset all UI state
                clearLoginForm()
                clearSignUpForm()
                clearEditProfileForm()
                clearDeleteAccountMessages()

                _deleteAccountState.value = DeleteAccountState(
                    isDeleting = false,
                    successMessage = "Account deleted successfully"
                )

            } catch (e: Exception) {
                _deleteAccountState.value = DeleteAccountState(
                    isDeleting = false,
                    errorMessage = "Error deleting account: ${e.message}"
                )
            }
        }
    }

    // LOG OUT
    fun logout() {
        viewModelScope.launch {
            // Clear session data
            dataStoreManager.clearSession()

            // Reset in-memory state
            _loggedInCustomer.value = null
            _uiState.value = UiState(isAuthChecking = false) // explicitly stop auth checking

            // Clear forms
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

    fun clearDeleteAccountMessages() {
        _deleteAccountState.value = DeleteAccountState()
    }

    suspend fun getStoredUserId(): String? {
        return dataStoreManager.getUserId().first()
    }
}