package com.example.cyhsalonappointment.screens.AdminLogin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cyhsalonappointment.screens.Admin.AdminViewModel

@Composable
fun AdminLoginScreen(
    viewModel: AdminViewModel,
    onBackButtonClicked: () -> Unit,
    onSuccess: () -> Unit
) {
    val scrollState = rememberScrollState()

    val loginState by viewModel.loginState.collectAsState()

    var adminId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF3E5F5),
            Color(0xFFEDE7F6),
            Color(0xFFE1BEE7)
        )
    )

    LaunchedEffect(loginState.isSuccess) {
        if (loginState.isSuccess) {
            onSuccess()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.clearLoginFields()
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .verticalScroll(scrollState)
                .systemBarsPadding()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = onBackButtonClicked,
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

            // Title
            Text(
                text = "Admin Login",
                fontSize = 33.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF6A1B9A),
                textAlign = TextAlign.Center,
                letterSpacing = 1.25.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
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
                    // Admin ID
                    OutlinedTextField(
                        value = loginState.adminId,
                        onValueChange = { viewModel.onAdminIdChange(it) },
                        label = { Text("Admin ID",
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF7B1FA2)) },
                        placeholder = { Text("Enter your admin ID", color = Color(0xFF9CA3AF)) },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Admin ID",
                                tint = Color(0xFF7B1FA2)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7B1FA2),
                            unfocusedBorderColor = Color(0xFFE1BEE7),
                            focusedLabelColor = Color(0xFF7B1FA2),
                            cursorColor = Color(0xFF7B1FA2)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Password
                    OutlinedTextField(
                        value = loginState.password,
                        onValueChange = { viewModel.onPasswordChange(it) },
                        label = { Text("Password",
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF7B1FA2)) },
                        placeholder = { Text("Enter your password", color = Color(0xFF9CA3AF)) },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password",
                                tint = Color(0xFF7B1FA2)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color(0xFF7B1FA2)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7B1FA2),
                            unfocusedBorderColor = Color(0xFFE1BEE7),
                            focusedLabelColor = Color(0xFF7B1FA2),
                            cursorColor = Color(0xFF7B1FA2)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(33.dp))

                    // Login button
                    ElevatedButton(
                        onClick = { viewModel.login() },
                        enabled = loginState.adminId.isNotBlank() && loginState.password.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Color(0xFF7B1FA2),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFE1BEE7),
                            disabledContentColor = Color(0xFF9C27B0)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp)
                    ) {
                        Text(
                            text = "Login",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

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