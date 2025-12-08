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
            Color(0xFFFEF9F3),
            Color(0xFFF5F0EA),
            Color(0xFFEFE8E0)
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
                .padding(horizontal = 24.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = onBackButtonClicked,
                    modifier = Modifier
                        .shadow(4.dp, RoundedCornerShape(12.dp))
                        .background(Color.White, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF446F5C),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Title
            Text(
                text = "Admin Login",
                fontSize = 33.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF446F5C),
                textAlign = TextAlign.Center,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Admin ID
                    OutlinedTextField(
                        value = loginState.adminId,
                        onValueChange = { viewModel.onAdminIdChange(it) },
                        label = { Text("Admin ID", fontWeight = FontWeight.Medium) },
                        placeholder = { Text("Enter your admin ID", color = Color(0xFF9CA3AF)) },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Admin ID",
                                tint = Color(0xFF446F5C)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF446F5C),
                            focusedLabelColor = Color(0xFF446F5C),
                            cursorColor = Color(0xFF446F5C)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Password
                    OutlinedTextField(
                        value = loginState.password,
                        onValueChange = { viewModel.onPasswordChange(it) },
                        label = { Text("Password", fontWeight = FontWeight.Medium) },
                        placeholder = { Text("Enter your password", color = Color(0xFF9CA3AF)) },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password",
                                tint = Color(0xFF446F5C)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color(0xFF6B7280)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF446F5C),
                            focusedLabelColor = Color(0xFF446F5C),
                            cursorColor = Color(0xFF446F5C)
                        ),
                        shape = RoundedCornerShape(12.dp)
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
                            containerColor = Color(0xFF446F5C),
                            contentColor = Color.White
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
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}