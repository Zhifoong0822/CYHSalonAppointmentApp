package com.example.cyhsalonappointment.screens.Logo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyhsalonappointment.R

@Composable
fun LogoScreen(onLoginButtonClicked: () -> Unit = {},
               onSignUpButtonClicked: () -> Unit = {},
               modifier: Modifier = Modifier){

    val scrollState = rememberScrollState()
    val topSpacing = 120.dp
    val logoSize = 270.dp
    val titleFontSize = 33.sp
    val subtitleFontSize = 17.sp
    val buttonHeight = 52.dp
    val buttonFontSize = 20.sp
    val contentPadding = 24.dp
    val buttonSpacing = 22.dp

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFEF9F3),
            Color(0xFFF5F0EA),
            Color(0xFFEFE8E0)
        )
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .background(brush = gradientBackground)
        .verticalScroll(scrollState)
        .padding(horizontal = contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally){

        Spacer(modifier = Modifier.height(topSpacing))

        //Logo
        Card(
            modifier = Modifier
                .size(logoSize)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color(0x1A446F5C),
                    spotColor = Color(0x1A446F5C)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
            color = Color(0xFF6B7280),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "CYH Salon",
            fontSize = titleFontSize,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF446F5C),
            textAlign = TextAlign.Center,
            letterSpacing = 1.25.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Your perfect salon awaits",
            fontSize = subtitleFontSize,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF9CA3AF),
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
                containerColor = Color(0xFF446F5C),
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
                contentColor = Color(0xFF446F5C)
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
}