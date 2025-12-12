package com.example.cyhsalonappointment.screens.BookingHistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cyhsalonappointment.BottomNavBar
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Duration

data class AppointmentDisplay(
    val appointmentId: String,
    val serviceName: String,
    val date: String,
    val timeSlotId: String,
    val status: String,
    val stylistName: String? = null,
    val hairLength: String? = null,
    val price: Double? = null
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryScreen(
    navController: NavHostController,
    viewModel: BookingHistoryViewModel,
    customerId: String,
    isAdmin: Boolean = false,
    selectedStatus: String? = null,
    onRescheduleClick: (AppointmentDisplay) -> Unit = {},
    onCancelClick: (AppointmentDisplay) -> Unit = {}
) {

    val appointments by viewModel.appointments.observeAsState(emptyList())
    val filteredAppointments = if (!selectedStatus.isNullOrEmpty()) {
        appointments.filter { it.status == selectedStatus }
    } else {
        appointments
    }

    // Load appointments once or whenever status changes
    LaunchedEffect(isAdmin, selectedStatus) {
        viewModel.loadAppointments(isAdmin = isAdmin, status = selectedStatus, customerId = customerId)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isAdmin) "Admin Booking History" else "My Booking History") }
            )
        },
        bottomBar = { if (!isAdmin) BottomNavBar(navController) else {} }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (filteredAppointments.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No booking history", fontSize = 18.sp, color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(appointments) { appt ->
                        HistoryCard(
                            appointment = appt,
                            isAdmin = isAdmin,
                            onRescheduleClick = onRescheduleClick,
                            onCancelClick = onCancelClick
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryCard(
    appointment: AppointmentDisplay,
    isAdmin: Boolean,
    onRescheduleClick: (AppointmentDisplay) -> Unit,
    onCancelClick: (AppointmentDisplay) -> Unit
) {
    val appointmentDate = LocalDate.parse(appointment.date)
    val appointmentTime = getStartTimeFromSlot(appointment.timeSlotId)
    val appointmentDateTime = LocalDateTime.of(appointmentDate, appointmentTime)
    val now = LocalDateTime.now()
    val allowCancel = Duration.between(now, appointmentDateTime).toHours() > 12

    var showConfirmDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Appointment ID: ${appointment.appointmentId}", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Service: ${appointment.serviceName}")
            appointment.stylistName?.let { Text("Stylist: $it") }
            appointment.hairLength?.let { Text("Hair Length: $it") }
            appointment.price?.let { Text("Price: RM %.2f".format(it)) }

            Spacer(modifier = Modifier.height(6.dp))
            val readableTime = appointmentTime.format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"))
            Text("Date: ${appointment.date}")
            Text("Time: $readableTime")

            Spacer(modifier = Modifier.height(10.dp))
            val statusColor = when (appointment.status) {
                "Completed" -> Color(0xFF4CAF50)
                "Today" -> Color(0xFFFF9800)
                "Upcoming" -> Color(0xFF2196F3)
                "Cancelled" -> Color.Gray
                else -> Color.Gray
            }
            Box(
                modifier = Modifier
                    .background(statusColor.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                    .padding(vertical = 4.dp, horizontal = 10.dp)
            ) {
                Text(appointment.status, color = statusColor, fontWeight = FontWeight.Medium, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (!isAdmin && appointment.status == "Upcoming") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { showConfirmDialog = true }, enabled = allowCancel) {
                        Text("Cancel", color = if (allowCancel) MaterialTheme.colorScheme.primary else Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(onClick = { onRescheduleClick(appointment) }) { Text("Reschedule") }
                }
            }
        }

        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = { Text("Cancel Booking?") },
                text = { Text("Are you sure you want to cancel this booking?") },
                confirmButton = {
                    TextButton(onClick = {
                        showConfirmDialog = false
                        onCancelClick(appointment)
                        showInfoDialog = true
                    }) { Text("Yes") }
                },
                dismissButton = { TextButton(onClick = { showConfirmDialog = false }) { Text("No") } }
            )
        }

        if (showInfoDialog) {
            AlertDialog(
                onDismissRequest = { showInfoDialog = false },
                title = { Text("Booking Cancelled") },
                text = { Text("The booking fee will not be refunded.") },
                confirmButton = { TextButton(onClick = { showInfoDialog = false }) { Text("OK") } }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getStartTimeFromSlot(timeSlotId: String): LocalTime {
    return when (timeSlotId) {
        "TS0001" -> LocalTime.of(10, 0)
        "TS0002" -> LocalTime.of(10, 30)
        "TS0003" -> LocalTime.of(11, 0)
        "TS0004" -> LocalTime.of(11, 30)
        "TS0005" -> LocalTime.of(12, 0)
        "TS0006" -> LocalTime.of(12, 30)
        "TS0007" -> LocalTime.of(13, 0)
        "TS0008" -> LocalTime.of(13, 30)
        "TS0009" -> LocalTime.of(14, 0)
        "TS0010" -> LocalTime.of(14, 30)
        "TS0011" -> LocalTime.of(15, 0)
        "TS0012" -> LocalTime.of(15, 30)
        "TS0013" -> LocalTime.of(16, 0)
        "TS0014" -> LocalTime.of(16, 30)
        "TS0015" -> LocalTime.of(17, 0)
        "TS0016" -> LocalTime.of(17, 30)
        else -> LocalTime.of(0, 0)
    }
}
