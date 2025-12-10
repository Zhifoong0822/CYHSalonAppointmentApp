
package com.example.cyhsalonappointment.screens.Account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSelectionScreen(
    navController: NavHostController
) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFEF9F3),
            Color(0xFFF5F0EA),
            Color(0xFFEFE8E0)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Profile Button - Navigate to "old_profile" (the actual profile screen)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clickable { navController.navigate("profile") },  // CHANGED FROM "profile" TO "old_profile"
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color(0xFF446F5C),
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "Profile",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF446F5C)
                    )
                }
            }

            // Payment History Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clickable { navController.navigate("paymentHistory") },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Icon(
                        Icons.Default.History,
                        contentDescription = "Payment History",
                        tint = Color(0xFF446F5C),
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "Payment History",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF446F5C)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
