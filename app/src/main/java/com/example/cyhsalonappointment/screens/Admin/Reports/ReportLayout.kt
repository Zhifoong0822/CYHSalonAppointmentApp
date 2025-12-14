package com.example.cyhsalonappointment.screens.Admin.Reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ReportLayout(
    title: String,
    totalSales: Double,
    totalAppointments: Int,
    average: Double,
    services: List<com.example.cyhsalonappointment.local.DAO.ServiceSalesReport>,
    onBack: () -> Unit
) {
    Column(Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp)) {

        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
        }

        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(20.dp))

        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            SummaryCard("Total Sales", "RM $totalSales")
            SummaryCard("Appointments", "$totalAppointments")
            SummaryCard("Avg/order", "RM ${String.format("%.2f", average)}")
        }

        Spacer(Modifier.height(18.dp))

        Text("Services Sales History", fontWeight = FontWeight.Bold)
        Divider()
        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text("Service                       ", fontWeight = FontWeight.Bold)
            Text("Date                 ", fontWeight = FontWeight.Bold)
            Text("Sales (RM)", fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(6.dp))

        services.forEach {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(it.serviceName)
                Text(it.date)
                Text("RM ${"%.2f".format(it.total)}")
            }
        }

    }
}

@Composable
fun SummaryCard(title: String, value: String) {
    Surface(tonalElevation = 3.dp, modifier = Modifier.padding(4.dp)) {
        Column(Modifier.padding(8.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(value)
        }
    }
}
