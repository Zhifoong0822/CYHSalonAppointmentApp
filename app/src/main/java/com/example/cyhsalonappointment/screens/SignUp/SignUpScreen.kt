package com.example.cyhsalonappointment.screens.SignUp

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cyhsalonappointment.screens.Customer.CustomerEntity
import com.example.cyhsalonappointment.screens.Customer.CustomerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(viewModel: CustomerViewModel = viewModel(),
                 onSuccess: (String) -> Unit,
                 onBackButtonClicked: () -> Unit,
                 modifier: Modifier = Modifier) {

    val signUpState by viewModel.signUpState.collectAsState()
    val scrollState = rememberScrollState()
    val shouldClearForm by viewModel.shouldClearSignUpForm.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val genderOptions = listOf("Male", "Female", "Prefer not to say")
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }

    val contentPadding = 24.dp
    val titleFontSize = 33.sp
    val buttonHeight = 52.dp
    val buttonFontSize = 16.sp

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF3E5F5),
            Color(0xFFEDE7F6),
            Color(0xFFE1BEE7)
        )
    )

    //clear only when shouldClearForm = true
    LaunchedEffect(shouldClearForm) {
        if (shouldClearForm) {
            viewModel.forceCleanState()
        }
    }

    LaunchedEffect(signUpState.successMessage) {
        signUpState.successMessage?.let { message ->
            // Navigate to LogoScreen and pass message as query parameter
            onSuccess(message)
            // Clear form and messages immediately in ViewModel
            viewModel.clearSignUpForm()
            viewModel.clearSignUpMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(brush = gradientBackground)
                .verticalScroll(scrollState)
                .systemBarsPadding()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            //Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = contentPadding),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = {
                        onBackButtonClicked()
                        viewModel.viewModelScope.launch {
                            delay(1000)
                            viewModel.clearSignUpForm()
                        }
                    },
                    modifier = Modifier
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = Color(0x33000000),
                            spotColor = Color(0x33000000)
                        )
                        .background(Color.White, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF7B1FA2),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF6A1B9A),
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.25.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = contentPadding)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color(0x33000000),
                        spotColor = Color(0x33000000)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    //Username
                    Column {
                        OutlinedTextField(
                            value = signUpState.username,
                            onValueChange = viewModel::onSignUpUsernameChange,
                            label = { Text("Username",
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF7B1FA2)) },
                            placeholder = {
                                Text(
                                    "Enter your username",
                                    color = Color(0xFF9CA3AF)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = "Username",
                                    tint = Color(0xFF7B1FA2)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF7B1FA2),
                                unfocusedBorderColor = Color(0xFFE1BEE7),
                                focusedLabelColor = Color(0xFF7B1FA2),
                                cursorColor = Color(0xFF7B1FA2),
                                errorBorderColor = Color(0xFFDC2626)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth(),
                            isError = !signUpState.usernameError.isNullOrEmpty()
                        )
                        if (!signUpState.usernameError.isNullOrEmpty()) {
                            Text(
                                text = signUpState.usernameError!!,
                                color = Color(0xFFC2185B),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }

                    //Gender
                    ExposedDropdownMenuBox(
                        expanded = genderExpanded,
                        onExpandedChange = { genderExpanded = !genderExpanded }
                    ) {
                        OutlinedTextField(
                            value = signUpState.gender,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Gender",
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF7B1FA2)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Wc,
                                    contentDescription = "Gender",
                                    tint = Color(0xFF7B1FA2)
                                )
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF7B1FA2),
                                unfocusedBorderColor = Color(0xFFE1BEE7),
                                focusedLabelColor = Color(0xFF7B1FA2),
                                cursorColor = Color(0xFF7B1FA2)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = genderExpanded,
                            onDismissRequest = { genderExpanded = false }
                        ) {
                            genderOptions.forEach { gender ->
                                DropdownMenuItem(
                                    text = { Text(text = gender, fontWeight = FontWeight.Medium) },
                                    onClick = {
                                        viewModel.onSignUpGenderChange(gender)
                                        genderExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Contact Number
                    OutlinedTextField(
                        value = signUpState.contactNumber,
                        onValueChange = { viewModel.onSignUpContactChange(it) }, // update ViewModel
                        label = { Text("Contact Number",
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF7B1FA2)) },
                        placeholder = {
                            Text(
                                "Enter your contact number",
                                color = Color(0xFF9CA3AF)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ContactPhone,
                                contentDescription = "Contact Number",
                                tint = Color(0xFF7B1FA2)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7B1FA2),
                            unfocusedBorderColor = Color(0xFFE1BEE7),
                            focusedLabelColor = Color(0xFF7B1FA2),
                            cursorColor = Color(0xFF7B1FA2)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    //Email
                    Column {
                        OutlinedTextField(
                            value = signUpState.email,
                            onValueChange = viewModel::onSignUpEmailChange,
                            label = { Text("Email Address",
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF7B1FA2)) },
                            placeholder = { Text("Enter your email", color = Color(0xFF9CA3AF)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Email,
                                    contentDescription = "Email",
                                    tint = Color(0xFF7B1FA2)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF7B1FA2),
                                unfocusedBorderColor = Color(0xFFE1BEE7),
                                focusedLabelColor = Color(0xFF7B1FA2),
                                cursorColor = Color(0xFF7B1FA2),
                                errorBorderColor = Color(0xFFDC2626)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth(),
                            isError = signUpState.emailError != null
                        )
                        if (signUpState.emailError != null) {
                            Text(
                                text = signUpState.emailError!!,
                                color = Color(0xFFC2185B),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }

                    //Password
                    Column {
                        OutlinedTextField(
                            value = signUpState.password,
                            onValueChange = viewModel::onSignUpPasswordChange,
                            label = { Text("Password",
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF7B1FA2)) },
                            placeholder = {
                                Text(
                                    "Enter your password",
                                    color = Color(0xFF9CA3AF)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = "Password",
                                    tint = Color(0xFF7B1FA2)
                                )
                            },
                            trailingIcon = {
                                val image =
                                    if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = image,
                                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                        tint = Color(0xFF7B1FA2)
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF7B1FA2),
                                unfocusedBorderColor = Color(0xFFE1BEE7),
                                focusedLabelColor = Color(0xFF7B1FA2),
                                cursorColor = Color(0xFF7B1FA2),
                                errorBorderColor = Color(0xFFDC2626)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth(),
                            isError = signUpState.passwordError != null
                        )
                        if (signUpState.passwordError != null) {
                            Text(
                                text = signUpState.passwordError!!,
                                color = Color(0xFFC2185B),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }

                    //Confirm Password
                    Column {
                        OutlinedTextField(
                            value = signUpState.confirmPassword,
                            onValueChange = viewModel::onSignUpConfirmPasswordChange,
                            label = { Text("Confirm Password",
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF7B1FA2)) },
                            placeholder = {
                                Text(
                                    "Confirm your password",
                                    color = Color(0xFF9CA3AF)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = "Confirm Password",
                                    tint = Color(0xFF7B1FA2)
                                )
                            },
                            trailingIcon = {
                                val image =
                                    if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                IconButton(onClick = {
                                    confirmPasswordVisible = !confirmPasswordVisible
                                }) {
                                    Icon(
                                        imageVector = image,
                                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                                        tint = Color(0xFF7B1FA2)
                                    )
                                }
                            },
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF7B1FA2),
                                unfocusedBorderColor = Color(0xFFE1BEE7),
                                focusedLabelColor = Color(0xFF7B1FA2),
                                cursorColor = Color(0xFF7B1FA2),
                                errorBorderColor = Color(0xFFDC2626)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth(),
                            isError = !signUpState.confirmPasswordError.isNullOrEmpty()
                        )
                        if (!signUpState.confirmPasswordError.isNullOrEmpty()) {
                            Text(
                                text = signUpState.confirmPasswordError!!,
                                color = Color(0xFFC2185B),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    //Sign Up Button
                    ElevatedButton(
                        onClick = {
                            val username = signUpState.username
                            val email = signUpState.email
                            val password = signUpState.password
                            val confirm = signUpState.confirmPassword
                            val contact = signUpState.contactNumber
                            val gender = signUpState.gender
                            val birthdate = signUpState.birthdate

                            when {
                                username.isBlank() -> {
                                    viewModel.onSignUpUsernameChange(username) // triggers error
                                    return@ElevatedButton
                                }
                                email.isBlank() -> {
                                    viewModel.onSignUpEmailChange(email)
                                    return@ElevatedButton
                                }
                                password.isBlank() -> {
                                    viewModel.onSignUpPasswordChange(password)
                                    return@ElevatedButton
                                }
                                password != confirm -> {
                                    viewModel.onSignUpConfirmPasswordChange(confirm)
                                    viewModel.setConfirmPasswordError("Passwords do not match")
                                    return@ElevatedButton
                                }
                                gender.isBlank() -> {
                                    viewModel.onSignUpGenderChange(gender)
                                    return@ElevatedButton
                                }
                            }

                            val newCustomer = CustomerEntity(
                                customerId = java.util.UUID.randomUUID().toString(),
                                username = username,
                                email = email,
                                password = password,
                                contactNumber = contact,
                                gender = gender,
                                createdAt = System.currentTimeMillis(),
                                updatedAt = System.currentTimeMillis()
                            )

                            viewModel.sign_up(newCustomer)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(buttonHeight),
                        enabled = !signUpState.isLoading,
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Color(0xFF7B1FA2),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFE1BEE7),
                            disabledContentColor = Color(0xFF9C27B0)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (signUpState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Signing Up...")
                        } else {
                            Text("Sign Up")
                        }
                    }

                    signUpState.errorMessage?.let { errorMessage ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFCE4EC)
                            )
                        ) {
                            Text(
                                text = errorMessage,
                                color = Color(0xFFC2185B),
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(12.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}