package com.example.cyhsalonappointment.screens.Logo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyhsalonappointment.R

@Composable
fun LogoScreen(message: String = "",
               onLoginButtonClicked: () -> Unit = {},
               onSignUpButtonClicked: () -> Unit = {},
               onAdminLoginButtonClicked: () -> Unit = {},
               modifier: Modifier = Modifier) {

    val snackbarHostState = remember { SnackbarHostState() }

    //Show sign-up successful snackbar
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
        }
    }

    val scrollState = rememberScrollState()
    val logoSize = 270.dp
    val titleFontSize = 33.sp
    val subtitleFontSize = 17.sp
    val buttonHeight = 52.dp
    val buttonFontSize = 20.sp
    val contentPadding = 24.dp
    val buttonSpacing = 22.dp

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF3E5F5),
            Color(0xFFEDE7F6),
            Color(0xFFE1BEE7)
        )
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .systemBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(
                        horizontal = contentPadding,
                        vertical = 32.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.height(5.dp))

                //Logo
                Card(
                    modifier = Modifier
                        .size(logoSize)
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
                    Image(
                        painter = painterResource(R.drawable.salon_logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(45.dp))

                Text(
                    text = "Welcome to",
                    fontSize = subtitleFontSize,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF7B1FA2),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "CYH Salon",
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF6A1B9A),
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.25.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Your perfect salon awaits",
                    fontSize = subtitleFontSize,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF9C27B0),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(55.dp))

                //Login Button
                ElevatedButton(
                    onClick = onLoginButtonClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(buttonHeight),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color(0xFF7B1FA2),
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Login",
                        fontSize = buttonFontSize,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(buttonSpacing))

                //Sign Up Button
                OutlinedButton(
                    onClick = onSignUpButtonClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(buttonHeight),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFF7B1FA2)
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 2.dp
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Sign Up",
                        fontSize = buttonFontSize,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .clickable { onAdminLoginButtonClicked() }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(60.dp)
                        .background(color = Color(0xFF7B1FA2), shape = CircleShape)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize(0.75f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin Login",
                            tint = Color.White,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Admin",
                    color = Color(0xFF7B1FA2),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}