package com.example.cyhsalonappointment.screens.BookingHistory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cyhsalonappointment.BottomNavBar


// -------------------- SAMPLE DATA MODEL --------------------
data class BookingHistoryItem(
    val id: String,
    val serviceName: String,
    val date: String,
    val time: String,
    val status: String
)

// -------------------- SAMPLE LIST --------------------
val sampleBookingHistory = listOf(
    BookingHistoryItem("1", "Haircut", "2025-01-12", "10:00 AM", "Completed"),
    BookingHistoryItem("2", "Hair Coloring", "2025-01-15", "02:00 PM", "Upcoming"),
    BookingHistoryItem("3", "Nail Spa", "2025-01-20", "11:30 AM", "Cancelled"),
)

// -------------------- BOOKING HISTORY + NAV BAR --------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryScreen(
    navController: NavHostController,
    onRescheduleClick: (BookingHistoryItem) -> Unit = {},
    onCancelClick: (BookingHistoryItem) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Booking History") }
                ,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(sampleBookingHistory) { item ->
                HistoryCard(
                    booking = item,
                    onRescheduleClick = onRescheduleClick,
                    onCancelClick = onCancelClick
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

// -------------------- CARD UI --------------------
@Composable
fun HistoryCard(
    booking: BookingHistoryItem,
    onRescheduleClick: (BookingHistoryItem) -> Unit,
    onCancelClick: (BookingHistoryItem) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = booking.serviceName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text("Date: ${booking.date}", fontSize = 14.sp)
            Text("Time: ${booking.time}", fontSize = 14.sp)

            Spacer(modifier = Modifier.height(10.dp))

            val statusColor = when (booking.status) {
                "Completed" -> Color(0xFF4CAF50)
                "Upcoming" -> Color(0xFF2196F3)
                "Cancelled" -> Color(0xFFF44336)
                else -> Color.Gray
            }

            Box(
                modifier = Modifier
                    .background(statusColor.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                    .padding(vertical = 4.dp, horizontal = 10.dp)
            ) {
                Text(
                    text = booking.status,
                    color = statusColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (booking.status == "Upcoming") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onCancelClick(booking) }) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(onClick = { onRescheduleClick(booking) }) {
                        Text("Reschedule")
                    }
                }
            }
        }
    }
}

