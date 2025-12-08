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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cyhsalonappointment.App
import com.example.cyhsalonappointment.screens.Booking.BookingViewModel
import com.example.cyhsalonappointment.screens.Booking.BookingViewModelFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TempPaymentScreen(
    navController: NavHostController,
    serviceName: String,
    selectedDate: String,
    selectedTimeSlot: String,
    stylistId: String,
    hairLength: String
) {
    val context = LocalContext.current
    val appointmentDao = App.db.appointmentDao()
    val bookingViewModel: BookingViewModel = viewModel(
        factory = BookingViewModelFactory(App.db.timeSlotDao(), appointmentDao)
    )

    // Extract just the HH:mm part if possible
    val safeTimeSlot = selectedTimeSlot.substringAfter("timeSlot=").substringBefore(")", selectedTimeSlot)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Temporary Payment Screen", style = MaterialTheme.typography.titleLarge)
        Text("Service: $serviceName")
        Text("Date: $selectedDate")
        Text("Time: $safeTimeSlot")
        Text("Stylist ID: $stylistId")
        Text("Hair Length: $hairLength")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // CREATE APPOINTMENT
                bookingViewModel.createAppointment(
                    date = selectedDate,
                    timeSlotId = safeTimeSlot, // use the safe string
                    customerId = "C0001",
                    serviceId = serviceName,
                    stylistId = stylistId
                )

                // Optional: schedule notification only if time parses correctly
                try {
                    val appointmentDateTime = LocalDateTime.of(
                        LocalDate.parse(selectedDate),
                        LocalTime.parse(safeTimeSlot)
                    )
                    bookingViewModel.scheduleNotification(context, appointmentDateTime)
                } catch (e: Exception) {
                    // ignore parsing errors
                }

                Toast.makeText(context, "Appointment booked!", Toast.LENGTH_SHORT).show()

                // Go back to service list
                navController.navigate("services") {
                    popUpTo("services") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pay & Confirm (Temporary)")
        }
    }
}
