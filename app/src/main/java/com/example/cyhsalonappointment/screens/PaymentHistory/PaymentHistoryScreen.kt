package com.example.cyhsalonappointment.screens.PaymentHistory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentHistoryScreen(
    navController: NavHostController,
    customerId: String
) {
    val paymentDao = App.db.paymentDao()
    val appointmentDao = App.db.appointmentDao()

    val viewModel: PaymentViewModel = viewModel(
        factory = PaymentViewModelFactory(paymentDao, appointmentDao)
    )

    val payments by viewModel.payments.collectAsState()

    // Store service names in a simple list
    val serviceNames = remember { mutableStateMapOf<String, String>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadPayments(customerId)
    }

    // When payments load, get service names
    LaunchedEffect(payments) {
        coroutineScope.launch {
            payments.forEach { payment ->
                if (!serviceNames.containsKey(payment.appointmentId)) {
                    val appointment = viewModel.getAppointment(payment.appointmentId)
                    serviceNames[payment.appointmentId] = appointment?.serviceName ?: "Unknown Service"
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment History") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (payments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No payment history",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(payments) { payment ->
                    // Get the service name for this payment
                    val serviceName = serviceNames[payment.appointmentId] ?: "Loading..."

                    PaymentHistoryCard(
                        payment = payment,
                        serviceName = serviceName
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentHistoryCard(
    payment: com.example.cyhsalonappointment.local.entity.Payment,
    serviceName: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val date = formatDate(payment.paymentDate)

            Text(
                text = "$date  |  ${payment.paymentTime}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Service: $serviceName",  // NOW THIS WILL SHOW!
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = "RM${"%.2f".format(payment.totalAmount)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF446F5C)
                )
            }

            Text(
                text = "Payment Method: ${payment.paymentMethod}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = "Status: ${payment.status}",
                fontSize = 12.sp,
                color = when (payment.status) {
                    "Successful" -> Color(0xFF4CAF50)
                    "PENDING" -> Color(0xFFFF9800)
                    else -> Color.Red
                }
            )
        }
    }
}

fun formatDate(dateString: String): String {
    return try {
        val input = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val output = java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale.getDefault())

        val parsed = input.parse(dateString)
        output.format(parsed!!)
    } catch (e: Exception) {
        dateString
    }
}