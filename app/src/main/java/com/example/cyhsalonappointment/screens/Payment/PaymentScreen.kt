package com.example.cyhsalonappointment.screens.Payment

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cyhsalonappointment.App
import com.example.cyhsalonappointment.screens.Booking.BookingViewModel
import com.example.cyhsalonappointment.screens.Booking.BookingViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavHostController,
    appointmentId: String,
    serviceName: String,
    servicePrice: Double,
    serviceId: Int,
    bookingDate: String,
    bookingTime: String,
    stylistId: String,
    customerId: String
) {
    var selectedPaymentMethod by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val appointmentDao = App.db.appointmentDao()
    val timeSlotDao = App.db.timeSlotDao()
    val bookingViewModel: BookingViewModel = viewModel(
        factory = BookingViewModelFactory(timeSlotDao, appointmentDao)
    )

    // Query TimeSlot by ID safely
    val timeSlot = remember(bookingTime) {
        runBlocking { timeSlotDao.getTimeSlotById(bookingTime) }
    }

    val showTime = timeSlot?.timeSlot ?: "Unknown"

    val stylistName = remember { mutableStateOf("Unknown Stylist") }
    val stylistLevel = remember { mutableStateOf("Beginner") }
    val isLoading = remember { mutableStateOf(true) }
    // Extract the actual time from the TimeSlot object string
    val displayTime = remember(bookingTime) {
        extractTimeFromTimeSlotString(bookingTime)
    }

    val paymentMethods = listOf(
        "Credit/Debit" to Icons.Default.CreditCard,
        "TNG" to Icons.Default.CreditCard,
        "PayPal" to Icons.Default.CreditCard
    )

    val taxAmount = servicePrice * 0.06
    val totalPayment = servicePrice + taxAmount
    val bookingFee = totalPayment * 0.10

    val date = try { LocalDate.parse(bookingDate) }
    catch (e: Exception) { LocalDate.now() }

    val formattedDate = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()) +
            " ${date.dayOfMonth} " +
            date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault()) +
            " ${date.year}"
    LaunchedEffect(stylistId) {
        if (!stylistId.isNullOrEmpty()) {
            withContext(Dispatchers.IO) {
                try {
                    val stylist = App.db.stylistDao().getStylistById(stylistId)
                    if (stylist != null) {
                        stylistName.value = stylist.stylistName
                        stylistLevel.value = stylist.stylistLevel
                    }
                    println("ReceiptScreen - Found stylist: ${stylistName.value}, Level: ${stylistLevel.value}")
                } catch (e: Exception) {
                    println("ReceiptScreen - Error fetching stylist: ${e.message}")
                } finally {
                    isLoading.value = false
                }
            }
        } else {
            isLoading.value = false
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment") },
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
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // -------------------- Booking Detail --------------------
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Booking Detail",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text("Service:", color = Color.Gray)
                        Text(serviceName.replace("+"," "))
                    }

                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text("Date:", color = Color.Gray)
                        Text(formattedDate)
                    }

                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text("Time:", color = Color.Gray)
                        Text(displayTime) // Changed from bookingTime to displayTime
                    }

                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text("Stylist :", color = Color.Gray)
                        Text("${stylistName.value} (${stylistLevel.value})")
                    }
                }
            }

            // -------------------- Payment Methods --------------------
            Text("Payment Methods", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            paymentMethods.forEach { (method, icon) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPaymentMethod = method },
                    shape = RoundedCornerShape(12.dp),
                    border = if (selectedPaymentMethod == method)
                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    else null
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(icon, contentDescription = method, tint = Color(0xFF446F5C))
                            Text(method, fontSize = 16.sp)
                        }

                        // Radio Indicator
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .border(
                                    2.dp,
                                    if (selectedPaymentMethod == method)
                                        MaterialTheme.colorScheme.primary
                                    else Color.Gray,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedPaymentMethod == method) {
                                Box(
                                    Modifier
                                        .size(12.dp)
                                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                                )
                            }
                        }
                    }
                }
            }

            // -------------------- Payment Summary --------------------
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text("Service Price:", color = Color.Gray)
                        Text("RM${"%.2f".format(servicePrice)}")
                    }

                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text("Tax (6%):", color = Color.Gray)
                        Text("RM${"%.2f".format(taxAmount)}")
                    }

                    Divider(color = Color.LightGray)

                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text("Total Service Fee:")
                        Text("RM${"%.2f".format(totalPayment)}")
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text("Booking Fee (10%):", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "RM${"%.2f".format(bookingFee)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF446F5C)
                        )
                    }
                }
            }

            // ----------------------- Pay Button -----------------------
            Button(
                onClick = {
                    bookingViewModel.createAppointment(
                        date = bookingDate,
                        timeSlotId = bookingTime,
                        customerId = customerId,
                        serviceId = serviceId,
                        stylistId = stylistId
                    )

                    Toast.makeText(context, "Appointment booked!", Toast.LENGTH_SHORT).show()
                    selectedPaymentMethod?.let { method ->

                        val encodedServiceName = Uri.encode(serviceName)
                        val encodedDate = Uri.encode(bookingDate)
                        val encodedTime = Uri.encode(displayTime)
                        val encodedMethod = Uri.encode(method)
                        val encodedStylist = Uri.encode(stylistId)

                        navController.navigate(
                            "receipt/$appointmentId/$encodedServiceName/${servicePrice.toFloat()}/" +
                                    "$encodedDate/$encodedTime/$encodedMethod/${bookingFee.toFloat()}/$encodedStylist"
                        )
                    }
                },

                enabled = selectedPaymentMethod != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Pay RM${"%.2f".format(bookingFee)}", fontSize = 18.sp)
            }
        }
    }
}


fun extractTimeFromTimeSlotString(timeSlotString: String): String {
    val timeSlotMap = mapOf(

        "TS0001" to "10:00",
        "TS0002" to "10:30",
        "TS0003" to "11:00",
        "TS0004" to "11:30",
        "TS0005" to "12:00",
        "TS0006" to "12:30",
        "TS0007" to "13:00",
        "TS0008" to "13:30",
        "TS0009" to "14:00",
        "TS0010" to "14:30",
        "TS0011" to "15:00",
        "TS0012" to "15:30",
        "TS0013" to "16:00",
        "TS0014" to "16:30",
        "TS0015" to "17:00",
        "TS0016" to "17:30",
        "TS0017" to "18:00"
    )

    return try {
        //  Extract actual time inside "timeslot= 10:30"
        if (timeSlotString.contains("timeslot=")) {
            val regex = """timeslot=\s*([^,)]+)""".toRegex()
            regex.find(timeSlotString)?.groupValues?.get(1)?.trim() ?: timeSlotString
        }

        //  Extract timeslotId=TS0002 → map to time
        else if (timeSlotString.contains("timeslotId=", ignoreCase = true)) {
            val regex = """timeslotId=\s*(TS\d{4})""".toRegex(RegexOption.IGNORE_CASE)
            val id = regex.find(timeSlotString)?.groupValues?.get(1)
            timeSlotMap[id] ?: timeSlotString
        }

        //  If input IS the ID — e.g., "TS0002"
        else if (timeSlotMap.containsKey(timeSlotString.trim())) {
            timeSlotMap[timeSlotString.trim()]!!
        }

        else timeSlotString

    } catch (e: Exception) {
        timeSlotString
    }
}
