package com.example.cyhsalonappointment.screens.Profile

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cyhsalonappointment.BottomNavBar
import com.example.cyhsalonappointment.R
import com.example.cyhsalonappointment.screens.Customer.CustomerViewModel

@Composable
fun ProfileScreen(
    customerEmail: String,
    navController: NavHostController,
    viewModel: CustomerViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onDeleteAccountClicked: () -> Unit = {},
    onLogoutClicked: () -> Unit = {},
    onEditProfileClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val editProfileState = viewModel.editProfileState.collectAsState()
    val deleteAccountState = viewModel.deleteAccountState.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var passwordInput by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Load profile
    LaunchedEffect(customerEmail) {
        if (customerEmail.isNotEmpty()) {
            viewModel.loadLocalUserProfile(customerEmail)
        }
    }

    LaunchedEffect(deleteAccountState.value) {
        Log.d("ProfileScreen", "DeleteAccountState changed: ${deleteAccountState.value}")
    }

    LaunchedEffect(deleteAccountState.value.successMessage) {
        deleteAccountState.value.successMessage?.let {
            showDeleteDialog = false
            passwordInput = ""
            passwordError = null

            viewModel.clearDeleteAccountMessages()

            navController.navigate("logo") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    LaunchedEffect(deleteAccountState.value.errorMessage) {
        Log.d("ProfileScreen", "Error message LaunchedEffect triggered: ${deleteAccountState.value.errorMessage}")
        deleteAccountState.value.errorMessage?.let { error ->
            Log.d("ProfileScreen", "Setting password error: $error")
            passwordError = error
        }
    }

    // Show snackbar after edit profile
    LaunchedEffect(editProfileState.value.successMessage) {
        editProfileState.value.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearEditProfileSuccessMessage()
            viewModel.clearEditProfileForm()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp)
        ) {

            // Title Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.weight(1f))

                // Edit Button
                OutlinedButton(
                    onClick = onEditProfileClicked,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Edit")
                }
            }

            Spacer(Modifier.height(20.dp))

            // Profile Picture
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(R.drawable.profile_pic),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.LightGray, CircleShape)
                )
            }

            Spacer(Modifier.height(30.dp))

            // User fields card
            uiState.value.userProfile?.let { profile ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(Color.White),
                    border = BorderStroke(1.dp, Color(0xFFDDDDDD)),
                    elevation = CardDefaults.cardElevation(0.dp)   // removed shadow
                ) {
                    Column(
                        Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ProfileField("Username", profile.username)
                        ProfileField("Gender", profile.gender)
                        ProfileField("Email", profile.email)
                        ProfileField("Contact Number", profile.contactNumber)
                    }
                }
            }

            Spacer(Modifier.height(40.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Delete Button
                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red
                    ),
                    border = BorderStroke(1.5.dp, Color.Red)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                    Spacer(Modifier.width(10.dp))
                    Text("Delete Account", color = Color.Red)
                }

                // Logout Button
                Button(
                    onClick = {
                        viewModel.logout()
                        onLogoutClicked()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(10.dp))
                    Text("Log out")
                }
            }
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                    passwordInput = ""
                    passwordError = null
                    viewModel.clearDeleteAccountMessages()
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val userId = uiState.value.userProfile?.customerId
                            val email = uiState.value.userProfile?.email ?: ""

                            if (passwordInput.isBlank()) {
                                passwordError = "Please enter your password"
                            } else if (!userId.isNullOrEmpty() && email.isNotEmpty()) {
                                viewModel.deleteAccount(userId, email, passwordInput)
                            }
                        },
                        enabled = !deleteAccountState.value.isDeleting
                    ) {
                        if (deleteAccountState.value.isDeleting) {
                            Text("Deleting...", color = Color.Red)
                        } else {
                            Text("Delete", color = Color.Red)
                        }
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            passwordInput = ""
                            passwordError = null
                            viewModel.clearDeleteAccountMessages()
                        },
                        enabled = !deleteAccountState.value.isDeleting
                    ) {
                        Text("Cancel")
                    }
                },
                title = { Text("Confirm Delete", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Enter your password to confirm:")
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = {
                                passwordInput = it
                                passwordError = null
                                viewModel.clearDeleteAccountMessages()
                            },
                            label = { Text("Password") },
                            isError = passwordError != null || deleteAccountState.value.errorMessage != null,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                    )
                                }
                            },
                            enabled = !deleteAccountState.value.isDeleting
                        )

                        // Show error message
                        val error = passwordError ?: deleteAccountState.value.errorMessage
                        if (error != null) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = error,
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String) {
    Column {
        Text(label, fontSize = 15.sp, color = Color.DarkGray)
        Spacer(Modifier.height(5.dp))
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
    }
}