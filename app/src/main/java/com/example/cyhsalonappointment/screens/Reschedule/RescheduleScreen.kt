package com.example.cyhsalonappointment.screens.Reschedule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.cyhsalonappointment.App
import com.example.cyhsalonappointment.local.entity.TimeSlot
import com.example.cyhsalonappointmentscreens.BookingScreen.DatePickerField
import com.example.cyhsalonappointmentscreens.BookingScreen.TimeSlotDropdown
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RescheduleScreen(
    navController: NavHostController,
    appointmentId: String,
    serviceName: String,
    oldDate: String,
    oldTime: String,
    parentBookingHistoryEntry: NavBackStackEntry
) {
    val timeSlotDao = App.db.timeSlotDao()
    val appointmentDao = App.db.appointmentDao()


    val viewModel: RescheduleViewModel = viewModel(
        viewModelStoreOwner = remember(parentBookingHistoryEntry) { parentBookingHistoryEntry },
        factory = RescheduleViewModelFactory(timeSlotDao, appointmentDao)
    )

    val availableTimeSlots by viewModel.timeSlots.collectAsState()

    var selectedDate by remember { mutableStateOf(LocalDate.parse(oldDate)) }
    var selectedTimeSlot by remember {
        mutableStateOf(availableTimeSlots.find { it.timeSlotId == oldTime })
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(appointmentId) {
        if (viewModel.isAlreadyRescheduled(appointmentId)) {
            snackbarHostState.showSnackbar(
                message = "This appointment has already been rescheduled."
            )
            navController.popBackStack()
        }
    }


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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Current Booking
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Current Booking", fontSize = 18.sp, color = Color.Gray)
                    Text("Service: $serviceName", fontSize = 20.sp)
                    Text("Date: $oldDate", fontSize = 16.sp)
                    Text(
                        "Time: ${availableTimeSlots.find { it.timeSlotId == oldTime }?.timeSlot ?: "Unknown"}",
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // New Date Picker
            Text("Select New Date", fontSize = 18.sp)
            DatePickerField(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )

            // New Time Slot
            Text("Select New Time Slot", fontSize = 18.sp)
            TimeSlotDropdown(
                timeSlots = availableTimeSlots,
                selectedSlot = selectedTimeSlot,
                selectedDate = selectedDate,
                onSelect = { selectedTimeSlot = it }
            )

            Spacer(modifier = Modifier.height(24.dp))
            val coroutineScope = rememberCoroutineScope()

            // Confirm Button
            Button(
                onClick = {
                    if (selectedTimeSlot != null) {
                        viewModel.updateAppointment(
                            appointmentId = appointmentId,
                            newDate = selectedDate.toString(),
                            newTimeSlotId = selectedTimeSlot!!.timeSlotId,
                            oldDate = oldDate,
                            oldTimeSlotId = oldTime,
                            onError = { message ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message)
                                }
                            },
                            onSuccess = {
                                navController.popBackStack()
                            }
                        )
                    }
                },
                enabled = selectedTimeSlot != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirm Reschedule")
            }
        }
    }
}
