package com.example.cyhsalonappointment.screens.Payment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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
    bookingDate: String,
    bookingTime: String, // This is "Timeslot(timeslotId=TS0002, timeslot= 10:30)"
    stylistId: String
) {
    var selectedPaymentMethod by remember { mutableStateOf<String?>(null) }

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
                        Text(serviceName)
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
                        Text("Stylist ID:", color = Color.Gray)
                        Text(stylistId)
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
                    selectedPaymentMethod?.let {
                        navController.navigate(
                            "receipt/$appointmentId/$serviceName/${servicePrice.toFloat()}/" +
                                    "$bookingDate/$displayTime/$it/${bookingFee.toFloat()}/$stylistId"
                        ) // Changed from bookingTime to displayTime
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
    return try {

        if (timeSlotString.startsWith("Timeslot(") || timeSlotString.startsWith("TimeSlot(")) {

            val timeslotPattern = """timeslot=\s*([^,)]+)""".toRegex(RegexOption.IGNORE_CASE)
            val match = timeslotPattern.find(timeSlotString)

            if (match != null) {
                val time = match.groupValues[1].trim()

                time.replace("\"", "").replace("'", "").trim()
            } else {

                val timePattern = """(\d{1,2}:\d{2})""".toRegex()
                timePattern.find(timeSlotString)?.value ?: timeSlotString
            }
        } else {

            timeSlotString
        }
    } catch (e: Exception) {

        timeSlotString
    }
}