package com.example.cyhsalonappointment.screens.BookingHistory

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cyhsalonappointment.BottomNavBar
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Duration
import com.example.cyhsalonappointment.R



// -------------------- SAMPLE DATA MODEL --------------------
data class AppointmentDisplay(
    val appointmentId: String,
    val serviceName: String,   // correct: serviceName
    val date: String,
    val timeSlotId: String,
    val status: String
)




@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryScreen(
    navController: NavHostController,
    viewModel: BookingHistoryViewModel,
    onRescheduleClick: (AppointmentDisplay) -> Unit = {},
    onCancelClick: (AppointmentDisplay) -> Unit = {}
) {
    val appointments by viewModel.appointments.observeAsState(emptyList())
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadAppointments()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Booking History") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (appointments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No booking history",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(appointments) { appt ->
                        HistoryCard(
                            appointment = appt,
                            onRescheduleClick = {
                                navController.navigate(
                                    "reschedule/${it.appointmentId}/${it.serviceName}/${it.date}/${it.timeSlotId}"
                                )
                            },
                            onCancelClick = { viewModel.cancelBooking(appt.appointmentId) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // ----------------- TEST BUTTON -----------------
                Button(
                    onClick = {
                        sendTestNotification(context)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Send Test Notification")
                }
            }
        }
    }
}




// -------------------- CARD UI --------------------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryCard(
    appointment: AppointmentDisplay,
    onRescheduleClick: (AppointmentDisplay) -> Unit,
    onCancelClick: (AppointmentDisplay) -> Unit
) {
    // --- NEW: calculate if cancel is allowed ---
    val appointmentDate = LocalDate.parse(appointment.date)
    val appointmentTime = getStartTimeFromSlot(appointment.timeSlotId)
    val appointmentDateTime = LocalDateTime.of(appointmentDate, appointmentTime)

    val now = LocalDateTime.now()

    // true if the booking starts in more than 12 hours
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
            Text(
                text = "Appointment ID: ${appointment.appointmentId}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text("Date: ${appointment.date}", fontSize = 14.sp)
            val readableTime = getStartTimeFromSlot(appointment.timeSlotId).format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"))
            Text("Time: $readableTime", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(10.dp))

            val statusColor = when (appointment.status) {
                "Completed" -> Color(0xFF4CAF50)
                "Today" -> Color(0xFFFF9800)
                "Upcoming" -> Color(0xFF2196F3)
                else -> Color.Gray
            }

            Box(
                modifier = Modifier
                    .background(statusColor.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                    .padding(vertical = 4.dp, horizontal = 10.dp)
            ) {
                Text(
                    text = appointment.status,
                    color = statusColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (appointment.status == "Upcoming") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    // CANCEL BUTTON
                    TextButton(
                        onClick = {
                            showConfirmDialog = true
                        },  // ← show dialog instead of directly cancelling
                        enabled = allowCancel
                    ) {
                        Text(
                            "Cancel",
                            color = if (allowCancel) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // RESCHEDULE BUTTON (Still allowed anytime)
                    Button(onClick = { onRescheduleClick(appointment) }) {
                        Text("Reschedule")
                    }
                }
            }
        }
        // Confirm cancellation dialog
        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = { Text("Cancel Booking?") },
                text = { Text("Are you sure you want to cancel this booking?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showConfirmDialog = false
                            onCancelClick(appointment)
                            showInfoDialog = true
                        }
                    ) { Text("Yes") }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDialog = false }) { Text("No") }
                }
            )
        }

        // Info dialog after cancellation
        if (showInfoDialog) {
            AlertDialog(
                onDismissRequest = { showInfoDialog = false },
                title = { Text("Booking Cancelled") },
                text = { Text("The booking fee will not be refunded.") },
                confirmButton = {
                    TextButton(onClick = { showInfoDialog = false }) { Text("OK") }
                }
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
            else -> LocalTime.of(0, 0) // fallback if invalid ID
        }
    }

fun sendTestNotification(context: Context) {
    val channelId = "booking_channel"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Booking Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications about upcoming or cancelled bookings"
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground) // <- make sure this exists
        .setContentTitle("Test Booking Notification")
        .setContentText("This is a test notification for your booking.")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()


    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(1001, notification)}


