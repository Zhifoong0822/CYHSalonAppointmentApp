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
import com.example.cyhsalonappointment.local.DAO.CustomerReport

@Composable
fun CustomerReportLayout(
    title: String,
    customers: List<CustomerReport>,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {

        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
        }

        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(20.dp))

        customers.forEach {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("Customer: ${it.customerId}")
                Text("Spent: RM ${it.totalSpent}")

            }
            Divider()
        }
    }
}
