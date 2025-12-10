package com.example.cyhsalonappointment.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cyhsalonappointment.App
import com.example.cyhsalonappointment.screens.Booking.BookingViewModel
import com.example.cyhsalonappointment.screens.Booking.BookingViewModelFactory
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TempPaymentScreen(
    navController: NavHostController,
    serviceId: Int,
    serviceName: String,
    selectedDate: String,
    selectedTimeSlotId: String,
    stylistId: String,
    hairLength: String
) {
    val context = LocalContext.current
    val appointmentDao = App.db.appointmentDao()
    val timeSlotDao = App.db.timeSlotDao()
    val bookingViewModel: BookingViewModel = viewModel(
        factory = BookingViewModelFactory(timeSlotDao, appointmentDao)
    )

    // Query TimeSlot by ID safely
    val timeSlot = remember(selectedTimeSlotId) {
        runBlocking { timeSlotDao.getTimeSlotById(selectedTimeSlotId) }
    }

    val displayTime = timeSlot?.timeSlot ?: "Unknown"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Confirm Appointment", style = MaterialTheme.typography.titleLarge)
        Text("Service: $serviceName")
        Text("Date: $selectedDate")
        Text("Time: $displayTime")
        Text("Stylist ID: $stylistId")
        Text("Hair Length: $hairLength")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                bookingViewModel.createAppointment(
                    date = selectedDate,
                    timeSlotId = selectedTimeSlotId,
                    customerId = "C0001",
                    serviceId = serviceId,
                    stylistId = stylistId
                )

                Toast.makeText(context, "Appointment booked!", Toast.LENGTH_SHORT).show()

                navController.navigate("services")
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = timeSlot != null // prevent booking if timeslot is missing
        ) {
            Text("Confirm")
        }
    }
}

