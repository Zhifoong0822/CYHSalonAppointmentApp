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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentHistoryScreen(
    navController: NavHostController
) {
    val paymentDao = App.db.paymentDao()
    val appointmentDao = App.db.appointmentDao()

    val viewModel: PaymentViewModel = viewModel(
        factory = PaymentViewModelFactory(paymentDao, appointmentDao)
    )

    // USE THE NEW paymentsWithServices INSTEAD OF payments
    val paymentsWithServices by viewModel.paymentsWithServices.collectAsState()

    LaunchedEffect(Unit) {
        // CALL THE NEW FUNCTION
        viewModel.loadPaymentsWithServiceNames()
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
        if (paymentsWithServices.isEmpty()) {
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
                // USE paymentsWithServices INSTEAD OF payments
                items(paymentsWithServices) { paymentWithService ->
                    PaymentHistoryCard(
                        payment = paymentWithService.payment,
                        appointment = paymentWithService.appointment
                    )
                }

            }
        }
    }
}

@Composable
fun PaymentHistoryCard(
    payment: com.example.cyhsalonappointment.local.entity.Payment,
    appointment:  com.example.cyhsalonappointment.local.entity.Appointment
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
                    text = "Service: ${appointment.serviceName}",
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
        val input = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val output = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        val parsed = input.parse(dateString)
        output.format(parsed!!)
    } catch (e: Exception) {
        dateString
    }
}