package com.example.cyhsalonappointment.screens.EditProfile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyhsalonappointment.screens.Customer.CustomerViewModel
import com.example.cyhsalonappointment.screens.Customer.UserProfile

@Composable
fun EditProfileScreen(
    viewModel: CustomerViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackButtonClicked: () -> Unit,
    onSaveSuccess: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val editProfileState by viewModel.editProfileState.collectAsState()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(editProfileState.successMessage) {
        editProfileState.successMessage?.let {
            onSaveSuccess()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = {
                        viewModel.clearEditProfileForm()
                        onBackButtonClicked()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Edit Profile",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.userProfile != null) {
                val profile = uiState.userProfile!!

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(Color.White),
                    border = BorderStroke(1.dp, Color(0xFFDDDDDD)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Column {
                            Text(
                                text = "Username",
                                fontSize = 15.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = profile.username.ifEmpty { "Not set" },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            //New Username
                            OutlinedTextField(
                                value = editProfileState.newUsername,
                                onValueChange = { viewModel.updateNewUsername(it) },
                                label = {
                                    Text(
                                        text = "New Username",
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                placeholder = {
                                    Text(
                                        text = "Enter new username",
                                        color = Color(0xFF9CA3AF)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Person,
                                        contentDescription = "Username",
                                        tint = Color(0xFF4CAF50)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !editProfileState.isLoading,
                                isError = editProfileState.errorMessage?.contains(
                                    "username",
                                    ignoreCase = true
                                ) == true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4CAF50),
                                    focusedLabelColor = Color(0xFF4CAF50),
                                    cursorColor = Color(0xFF4CAF50),
                                    errorBorderColor = Color(0xFFDC2626)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Column {
                            Text(
                                text = "Gender",
                                fontSize = 15.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = profile.gender.ifEmpty { "Not set" },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            //New Gender
                            OutlinedTextField(
                                value = editProfileState.newGender,
                                onValueChange = {},
                                readOnly = true,
                                label = {
                                    Text(
                                        text = "New Gender",
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                placeholder = {
                                    Text(
                                        text = "Select new gender",
                                        color = Color(0xFF9CA3AF)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Wc,
                                        contentDescription = "Gender",
                                        tint = Color(0xFF4CAF50)
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { expanded = !expanded }) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Dropdown",
                                            tint = Color(0xFF6B7280)
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { expanded = !expanded },
                                enabled = !editProfileState.isLoading,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4CAF50),
                                    focusedLabelColor = Color(0xFF4CAF50),
                                    cursorColor = Color(0xFF4CAF50)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )

                            //Gender Dropdown Menu
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                listOf(
                                    "Male",
                                    "Female",
                                    "Prefer not to say"
                                ).forEach { genderOption ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                genderOption,
                                                fontWeight = FontWeight.Medium
                                            )
                                        },
                                        onClick = {
                                            viewModel.updateNewGender(genderOption)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        //Email Section (Read-only)
                        Column {
                            Text(
                                text = "Email",
                                fontSize = 15.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = profile.email.ifEmpty { "Not set" },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = "Info",
                                        tint = Color(0xFF92400E),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Email cannot be changed",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF92400E),
                                        style = androidx.compose.ui.text.TextStyle(
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Column {
                            Text("Contact Number", fontSize = 15.sp, color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(profile.contactNumber.ifEmpty { "Not set" }, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = editProfileState.newContactNumber,
                                onValueChange = { viewModel.onEditProfileContactChange(it) },
                                label = { Text("New Contact Number", fontWeight = FontWeight.Medium) },
                                placeholder = { Text("Enter new contact number", color = Color(0xFF9CA3AF)) },
                                leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = "Contact", tint = Color(0xFF4CAF50)) },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !editProfileState.isLoading,
                                isError = editProfileState.errorMessage?.contains("contact", ignoreCase = true) == true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4CAF50),
                                    focusedLabelColor = Color(0xFF4CAF50),
                                    cursorColor = Color(0xFF4CAF50),
                                    errorBorderColor = Color.Red
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )

                            // Inline error message
                            editProfileState.errorMessage?.let { errorMsg ->
                                if (errorMsg.contains("contact", ignoreCase = true)) {
                                    Text(
                                        text = errorMsg,
                                        color = Color.Red,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                        Spacer(modifier = Modifier.height(20.dp))

                //Save Button
                Button(
                    onClick = {
                        val updatedProfile = UserProfile(
                            customerId = profile.customerId,
                            username = if (editProfileState.newUsername.isNotBlank()) editProfileState.newUsername.trim() else profile.username,
                            email = profile.email,
                            gender = if (editProfileState.newGender.isNotBlank()) editProfileState.newGender.trim() else profile.gender,
                            contactNumber = if (editProfileState.newContactNumber.isNotBlank()) editProfileState.newContactNumber.trim() else profile.contactNumber,
                            createdAt = profile.createdAt,
                            updatedAt = profile.updatedAt
                        )
                        viewModel.updateUserProfile(updatedProfile)
                    },
                    enabled = !editProfileState.isLoading &&
                            editProfileState.errorMessage.isNullOrEmpty() &&
                            (editProfileState.newUsername.isNotBlank() ||
                                    editProfileState.newGender.isNotBlank() ||
                                    editProfileState.newEmail.isNotBlank() ||
                                    editProfileState.newContactNumber.isNotBlank()),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFE5E7EB),
                        disabledContentColor = Color(0xFF9CA3AF)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    if (editProfileState.isLoading) {
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
                                text = "Saving...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        Text(
                            text = "Save Changes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                editProfileState.errorMessage?.let { errorMsg ->
                    if (!errorMsg.contains("contact", ignoreCase = true)) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFEE2E2)
                            )
                        ) {
                            Text(
                                text = errorMsg,
                                color = Color(0xFFDC2626),
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(12.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(Color.White),
                    border = BorderStroke(1.dp, Color(0xFFDDDDDD)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF4CAF50),
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading profile...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF6B7280)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}