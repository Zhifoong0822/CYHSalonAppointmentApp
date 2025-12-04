
package com.example.cyhsalonappointmentscreens.BookingScreen

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.time.LocalDate
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cyhsalonappointment.App
import com.example.cyhsalonappointment.local.entity.TimeSlot
import com.example.cyhsalonappointment.screens.Booking.BookingViewModel
import com.example.cyhsalonappointment.screens.Booking.BookingViewModelFactory
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    navController: NavHostController,
    serviceName: String
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTimeSlot by remember { mutableStateOf<TimeSlot?>(null) }
    val context = LocalContext.current
    val timeSlotDao = App.db.timeSlotDao()
    val appointmentDao = App.db.appointmentDao()

    val viewModel: BookingViewModel = viewModel(
        factory = BookingViewModelFactory(timeSlotDao, appointmentDao)
    )

    // Collect timeslots from DB
    val availableTimeSlots by viewModel.timeSlots.collectAsState()




    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Service") },
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

            // SERVICE NAME
            Text(serviceName, fontSize = 22.sp, style = MaterialTheme.typography.titleLarge)

            DatePickerField(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )


            // TIME SLOT DROPDOWN
            if (availableTimeSlots.isNotEmpty()) {
                TimeSlotDropdown(
                    timeSlots = availableTimeSlots,
                    selectedSlot = selectedTimeSlot,
                    selectedDate = selectedDate,
                    onSelect = { selectedTimeSlot = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    navController.navigate("selectStylist/$serviceName/$selectedDate/${selectedTimeSlot!!.timeSlot}")



                },
                enabled = selectedTimeSlot != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Choose Stylist")
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDatePicker(
                    context = context,
                    currentDate = selectedDate,
                    onDateSelected = onDateSelected
                )
            }
    ) {
        OutlinedTextField(
            value = selectedDate.toString(),
            onValueChange = {},
            readOnly = true,
            enabled = false, // disable interaction so Box click works
            label = { Text("Select Date") },
            trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = LocalContentColor.current,
                disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        )
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSlotDropdown(
    timeSlots: List<TimeSlot>,          // <- pass TimeSlot objects
    selectedSlot: TimeSlot?,            // <- selected object
    selectedDate: LocalDate,
    onSelect: (TimeSlot) -> Unit
) {
    val today = LocalDate.now()
    var expanded by remember { mutableStateOf(false) }

    val filteredSlots by remember(timeSlots, selectedDate) {
        derivedStateOf {
            timeSlots.filter { slot ->
                if (selectedDate != today) {
                    true
                } else {
                    val now = LocalTime.now() // <-- move here!
                    val slotTime = LocalTime.parse(slot.timeSlot)
                    slotTime.isAfter(now)
                }
            }
        }
    }

    Column {
        if (filteredSlots.isEmpty()) {
            Text(
                "No available time slots",
                color = Color.Red,
                fontSize = 14.sp
            )
        } else {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedSlot?.timeSlot ?: "",   // display "10:00"
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Time Slot") },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    filteredSlots.forEach { slot ->
                        DropdownMenuItem(
                            text = { Text(slot.timeSlot) },
                            onClick = {
                                onSelect(slot)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
fun showDatePicker(
    context: Context,
    currentDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            val selectedDate = LocalDate.of(year, month + 1, day)
            // Only accept today or future dates
            if (!selectedDate.isBefore(LocalDate.now())) {
                onDateSelected(selectedDate)
            } else {
                Toast.makeText(context, "Cannot select past date", Toast.LENGTH_SHORT).show()
            }
        },
        currentDate.year,
        currentDate.monthValue - 1,
        currentDate.dayOfMonth
    )

    // Set the minimum date to today (in milliseconds)
    val calendar = java.util.Calendar.getInstance()
    calendar.set(
        LocalDate.now().year,
        LocalDate.now().monthValue - 1,
        LocalDate.now().dayOfMonth
    )
    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    datePickerDialog.show()
}




