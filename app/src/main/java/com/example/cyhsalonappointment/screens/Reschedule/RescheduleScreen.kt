package com.example.cyhsalonappointment.screens.Reschedule

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cyhsalonappointmentscreens.BookingScreen.DatePickerField
import com.example.cyhsalonappointmentscreens.BookingScreen.TimeSlotDropdown
import com.example.cyhsalonappointmentscreens.BookingScreen.showDatePicker
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RescheduleScreen(
    navController: NavHostController,
    serviceName: String,
    oldDate: String,
    oldTime: String,
    availableTimeSlots: List<String> = listOf(
        "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM"
    )
) {
    val context = LocalContext.current

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTimeSlot by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reschedule") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // CURRENT BOOKING DETAILS
            Text("Current Booking", fontSize = 20.sp, style = MaterialTheme.typography.titleMedium)
            Text("Service: $serviceName")
            Text("Date: $oldDate")
            Text("Time: $oldTime")

            Spacer(modifier = Modifier.height(12.dp))

            Text("Select New Date", fontSize = 20.sp, style = MaterialTheme.typography.titleMedium)

            DatePickerField(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )

            TimeSlotDropdown(
                timeSlots = availableTimeSlots,
                selectedSlot = selectedTimeSlot,
                onSelect = { selectedTimeSlot = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Confirm reschedule (you would update DB or navigate back)
                    navController.popBackStack()
                },
                enabled = selectedTimeSlot.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirm Reschedule")
            }
        }
    }
}


