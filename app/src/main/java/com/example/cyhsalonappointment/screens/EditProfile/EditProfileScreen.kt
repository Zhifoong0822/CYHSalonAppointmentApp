package com.example.cyhsalonappointment.screens.EditProfile

import android.util.Log
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

    val contentPadding = 24.dp
    val titleFontSize = 32.sp
    val buttonHeight = 52.dp
    val buttonFontSize = 18.sp

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFEF9F3),
            Color(0xFFF5F0EA),
            Color(0xFFEFE8E0)
        )
    )

    LaunchedEffect(editProfileState.successMessage) {
        editProfileState.successMessage?.let {
            onSaveSuccess()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF446F5C)),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = snackbarData.visuals.message,
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = modifier
                .background(brush = gradientBackground)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            //Back Button
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 22.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = {
                        viewModel.clearEditProfileForm()
                        onBackButtonClicked()
                    },
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

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Edit Profile",
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF446F5C),
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            uiState.userProfile?.let { profile ->

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
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        //Username
                        Column {
                            Text(
                                text = "Username",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF374151)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Current:",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF6B7280)
                                    )
                                    Text(
                                        text = profile.username.ifEmpty { "Not set" },
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF111827)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

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
                                        tint = Color(0xFF446F5C)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !editProfileState.isLoading,
                                isError = editProfileState.errorMessage?.contains("username", ignoreCase = true) == true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF446F5C),
                                    focusedLabelColor = Color(0xFF446F5C),
                                    cursorColor = Color(0xFF446F5C),
                                    errorBorderColor = Color(0xFFDC2626)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        //Gender
                        Column {
                            Text(
                                text = "Gender",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF374151)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Current:",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF6B7280)
                                    )
                                    Text(
                                        text = profile.gender.ifEmpty { "Not set" },
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF111827)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

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
                                        tint = Color(0xFF446F5C)
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
                                    focusedBorderColor = Color(0xFF446F5C),
                                    focusedLabelColor = Color(0xFF446F5C),
                                    cursorColor = Color(0xFF446F5C)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            //Gender Dropdown Menu
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                listOf("Male", "Female", "Prefer not to say").forEach { genderOption ->
                                    DropdownMenuItem(
                                        text = { Text(genderOption, fontWeight = FontWeight.Medium) },
                                        onClick = {
                                            viewModel.updateNewGender(genderOption)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        //Email Section (Read-only)
                        Column {
                            Text(
                                text = "Email Address",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF374151)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Email,
                                            contentDescription = "Email",
                                            tint = Color(0xFF6B7280),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = profile.email.ifEmpty { "Not set" },
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF111827)
                                        )
                                    }
                                }
                            }

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
                                        text = "Email cannot be changed from this screen",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF92400E),
                                        style = androidx.compose.ui.text.TextStyle(
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                        )
                                    )
                                }
                            }
                        }

                        //Contact Number
                        Column {
                            Text(
                                text = "Contact Number",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF374151)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Current:",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF6B7280)
                                    )
                                    Text(
                                        text = profile.contactNumber.ifEmpty { "Not set" },
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF111827)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            //New Contact Number
                            OutlinedTextField(
                                value = editProfileState.newContactNumber,
                                onValueChange = { viewModel.updateNewContactNumber(it) },
                                label = {
                                    Text(
                                        "New Contact Number",
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                placeholder = {
                                    Text(
                                        "Enter new contact number",
                                        color = Color(0xFF9CA3AF)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Phone,
                                        contentDescription = "Contact",
                                        tint = Color(0xFF446F5C)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !editProfileState.isLoading,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF446F5C),
                                    focusedLabelColor = Color(0xFF446F5C),
                                    cursorColor = Color(0xFF446F5C),
                                    errorBorderColor = Color.Red
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        //Save Button
                        ElevatedButton(
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
                                    (editProfileState.newUsername.isNotBlank() ||
                                            editProfileState.newGender.isNotBlank() ||
                                            editProfileState.newEmail.isNotBlank() ||
                                            editProfileState.newContactNumber.isNotBlank()),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = Color(0xFF446F5C),
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFFE5E7EB),
                                disabledContentColor = Color(0xFF9CA3AF)
                            ),
                            elevation = ButtonDefaults.elevatedButtonElevation(
                                defaultElevation = 6.dp,
                                pressedElevation = 2.dp,
                                disabledElevation = 0.dp
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(buttonHeight)
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
                                        fontSize = buttonFontSize,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            } else {
                                Text(
                                    text = "Save Changes",
                                    fontSize = buttonFontSize,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        //Error Message
                        editProfileState.errorMessage?.let { errorMsg ->
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
                }
            } ?: run {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF446F5C),
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
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}