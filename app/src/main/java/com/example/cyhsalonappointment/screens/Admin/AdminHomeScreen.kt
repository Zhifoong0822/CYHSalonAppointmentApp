package com.example.cyhsalonappointment.screens.Admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip

@Composable
fun AdminHomeScreen(
    onManageServices: () -> Unit,
    onGenerateDailyReport: () -> Unit,
    onGenerateWeeklyReport: () -> Unit,
    onGenerateMonthlyReport: () -> Unit,
    onGenerateCustomerReport: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        // HEADER
        Text("My Shop", fontSize = 26.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(20.dp))

        // PROFILE
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(60.dp).clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Text("Yu He", fontSize = 20.sp, fontWeight = FontWeight.Medium)
        }

        Spacer(Modifier.height(25.dp))

        Text("Order Status", fontSize = 18.sp, fontWeight = FontWeight.Black)

        Spacer(Modifier.height(12.dp))

        // STATUS BUTTONS
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            StatusChip("On Going")
            StatusChip("Canceled")
            StatusChip("Completed")
        }

        Spacer(Modifier.height(28.dp))

        // MY SERVICES
        MenuButton("My Services", onManageServices)

        // GENERATE REPORT DROPDOWN
        Spacer(Modifier.height(6.dp))
        MenuButton(
            title = "Generate Report",
            onClick = { expanded = !expanded },
            showIcon = true,
            expanded = expanded
        )

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(Modifier.padding(start = 20.dp, top = 10.dp)) {

                SubMenuItem("Generate Daily Report", onGenerateDailyReport)
                SubMenuItem("Generate Weekly Report", onGenerateWeeklyReport)
                SubMenuItem("Generate Monthly Report", onGenerateMonthlyReport)
                SubMenuItem("Generate Customer Report", onGenerateCustomerReport)
            }
        }
    }
}

@Composable
fun MenuButton(title: String, onClick: () -> Unit, showIcon: Boolean = false, expanded: Boolean = false) {
    Surface(
        tonalElevation = 3.dp,
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.weight(1f))

            if (showIcon) {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun SubMenuItem(title: String, onClick: () -> Unit) {
    Text(
        text = title,
        fontSize = 15.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(10.dp)
    )
}

@Composable
fun StatusChip(title: String) {
    Surface(
        color = Color(0xFFE3E3E3),
        shape = CircleShape,
        modifier = Modifier.size(90.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = title, fontSize = 13.sp)
        }
    }
}
