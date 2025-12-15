package com.example.cyhsalonappointment.screens.Login

import android.util.Log
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cyhsalonappointment.screens.Customer.CustomerRepository
import com.example.cyhsalonappointment.screens.Customer.CustomerViewModel
import com.example.cyhsalonappointment.screens.Customer.CustomerViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: CustomerViewModel = viewModel(),
                onBackButtonClicked: () -> Unit,
                onSuccess: () -> Unit,
                modifier: Modifier = Modifier){

    val loginState by viewModel.loginState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val loggedInCustomer by viewModel.loggedInCustomer.collectAsState()
    val shouldClearForm = loggedInCustomer == null
    val scrollState = rememberScrollState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val contentPadding = 24.dp
    val titleFontSize = 33.sp
    val buttonHeight = 52.dp
    val buttonFontSize = 18.sp
    val forgotPasswordFontSize = 15.sp

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
            Log.d("LoginScreen", "Clearing form due to navigation")
            viewModel.forceCleanState()
        }
    }

    LaunchedEffect(loginState.successMessage) {
        if (loginState.successMessage != null) {
            onSuccess()
            viewModel.clearLoginMessages()
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
            Spacer(modifier = Modifier.height(16.dp))

            //Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = contentPadding, vertical = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = {
                        onBackButtonClicked()
                        viewModel.viewModelScope.launch {
                            delay(1000)
                            viewModel.clearLoginForm()
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

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Login",
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF6A1B9A),
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.25.sp
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

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
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Email
                    OutlinedTextField(
                        value = loginState.email,
                        onValueChange = viewModel::onLoginEmailChange,
                        label = {
                            Text("Email Address",
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF7B1FA2))
                        },
                        placeholder = { Text("Enter your email", color = Color(0xFF9CA3AF)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
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
                            cursorColor = Color(0xFF7B1FA2)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    //Password
                    OutlinedTextField(
                        value = loginState.password,
                        onValueChange = viewModel::onLoginPasswordChange,
                        label = {
                            Text("Password",
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF7B1FA2))
                        },
                        placeholder = { Text("Enter your password", color = Color(0xFF9CA3AF)) },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Password",
                                tint = Color(0xFF7B1FA2)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7B1FA2),
                            unfocusedBorderColor = Color(0xFFE1BEE7),
                            focusedLabelColor = Color(0xFF7B1FA2),
                            cursorColor = Color(0xFF7B1FA2)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(35.dp))

                    //Login Button
                    ElevatedButton(
                        onClick = viewModel::login,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(buttonHeight),
                        enabled = !loginState.isLoading &&
                                loginState.email.isNotBlank() &&
                                loginState.password.isNotBlank(),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Color(0xFF7B1FA2),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFE1BEE7),
                            disabledContentColor = Color(0xFF9C27B0)
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 2.dp,
                            disabledElevation = 0.dp
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (loginState.isLoading) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Logging In...",
                                    fontSize = buttonFontSize,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        } else {
                            Text(
                                text = "Login",
                                fontSize = buttonFontSize,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    // Error message card
                    loginState.errorMessage?.let { errorMessage ->
                        Spacer(modifier = Modifier.height(16.dp))
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