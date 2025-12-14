
package com.example.cyhsalonappointment.screens.Receipt

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cyhsalonappointment.App
import com.example.cyhsalonappointment.screens.Payment.PaymentViewModel
import com.example.cyhsalonappointment.screens.Payment.PaymentViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(
    navController: NavHostController,
    appointmentId: String,
    serviceName: String,
    servicePrice: Double,
    bookingDate: String,
    bookingTime: String,
    paymentMethod: String,
    totalAmount: Double,
    stylistId: String? = null
) {
    val paymentDao = App.db.paymentDao()
    val appointmentDao = App.db.appointmentDao()

    val paymentViewModel: PaymentViewModel = viewModel(
        factory = PaymentViewModelFactory(paymentDao, appointmentDao)

    )

    // State for stylist info
    val stylistName = remember { mutableStateOf("Unknown Stylist") }
    val stylistLevel = remember { mutableStateOf("Beginner") }
    val isLoading = remember { mutableStateOf(true) }

    // Generate receipt data
    val currentDateTime = LocalDateTime.now()
    val receiptDate = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val receiptTime = currentDateTime.format(DateTimeFormatter.ofPattern("hh:mm a"))


    val taxAmount = servicePrice * 0.06
    val bookingFee = (taxAmount + servicePrice) * 0.1
    val amountPaidToday = bookingFee
    val balanceAfter = servicePrice + taxAmount-bookingFee

    // Get stylist info from database
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

    LaunchedEffect(Unit) {
        try {
            // Generate payment ID
            val paymentCount = paymentViewModel.getPaymentCount()
            val paymentId = "PAY${(paymentCount + 1).toString().padStart(4, '0')}"

            println("ReceiptScreen - Creating payment: $paymentId for appointment: $appointmentId")

            // Create payment record
            paymentViewModel.createPayment(
                paymentId = paymentId,
                appointmentId = appointmentId,
                purchaseAmount = servicePrice,
                bookingFee = bookingFee,
                taxAmount = taxAmount,
                totalAmount = totalAmount,
                paymentMethod = paymentMethod,
                paymentDate = receiptDate,
                paymentTime = receiptTime
            )

            val cleanServiceName = serviceName.replace("+", " ")

            appointmentDao.attachDetails(
                appointmentId = appointmentId,
                finalPrice = totalAmount,
                serviceName = cleanServiceName,
                customerName = null
            )



            println("ReceiptScreen - Payment saved successfully")
        } catch (e: Exception) {
            println("ReceiptScreen - Error saving payment: ${e.message}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Receipt") }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    navController.navigate("services") {
                        popUpTo("services") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Done", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
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

            // Success Icon
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Successful",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(80.dp)
                )
            }

            Text(
                text = "Payment Successful!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50),
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Text(
                text = "Receipt",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Business Address
            Text(
                text = "1 Jalan Genting Kelang,\nSetapak 53300,\nKuala Lumpur",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Receipt Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Date:", color = Color.Gray)
                Text(receiptDate, fontWeight = FontWeight.Medium)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Time:", color = Color.Gray)
                Text(receiptTime, fontWeight = FontWeight.Medium)
            }

            // Show Stylist Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Stylist:", color = Color.Gray)
                if (isLoading.value) {
                    Text("Loading...", fontWeight = FontWeight.Medium)
                } else {
                    Text("${stylistName.value} (${stylistLevel.value})", fontWeight = FontWeight.Medium)
                }
            }

            Divider(color = Color.Gray.copy(alpha = 0.3f))

            // Service Table - Enhanced to show stylist name
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Description", fontWeight = FontWeight.Bold)
                    Text("Qty", fontWeight = FontWeight.Bold)
                    Text("Price/(RM)", fontWeight = FontWeight.Bold)
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Service Row - Shows service with stylist level
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("$serviceName")
                        if (!isLoading.value) {
                            Text(
                                "by ${stylistName.value} (${stylistLevel.value})",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    Text("1")
                    Text("%.2f".format(servicePrice))
                }
            }

            Divider(color = Color.Gray.copy(alpha = 0.3f))

            // Price Breakdown
            Text("Price Breakdown", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Service Total:", color = Color.Gray)
                Text("RM${"%.2f".format(servicePrice)}", fontWeight = FontWeight.Medium)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tax(6%):", color = Color.Gray)
                Text("RM${"%.2f".format(taxAmount)}", fontWeight = FontWeight.Medium)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Service Fee:", color = Color.Gray)
                Text("RM${"%.2f".format(servicePrice + taxAmount)}", fontWeight = FontWeight.Medium)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Booking Fee(10%):", color = Color.Gray)
                Text("RM${"%.2f".format(bookingFee)}", fontWeight = FontWeight.Medium)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Amount paid today:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    "RM${"%.2f".format(amountPaidToday)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF446F5C)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Balance After Appointment:", color = Color.Gray)
                Text("RM${"%.2f".format(balanceAfter)}", fontWeight = FontWeight.Medium)
            }

            Divider(color = Color.Gray.copy(alpha = 0.3f))

            // Payment Method
            Text("Payment Method", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(paymentMethod, fontSize = 16.sp, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
