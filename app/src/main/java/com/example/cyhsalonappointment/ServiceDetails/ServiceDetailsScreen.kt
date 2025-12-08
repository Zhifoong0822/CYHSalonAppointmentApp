package com.example.cyhsalonappointment.ServiceDetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cyhsalonappointment.local.entity.SalonService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailsScreen(
    navController: NavHostController,
    services: List<SalonService>,
    selectedDate: String,
    selectedTimeSlot: String
) {
    var selectedServiceId by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Select Service Details", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (services.isEmpty()) {
            // Show message if no services available
            Text(
                "No service details available.",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            // Display each service
            services.forEach { service ->
                val isSelected = service.serviceId == selectedServiceId

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { selectedServiceId = service.serviceId },
                    shape = RoundedCornerShape(12.dp),
                    border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(service.serviceName, style = MaterialTheme.typography.titleMedium)

                        // Display all non-null prices
                        val priceTexts = mutableListOf<String>()
                        service.priceShort?.let { priceTexts.add("Short Hair: RM $it") }
                        service.priceMedium?.let { priceTexts.add("Medium Hair: RM $it") }
                        service.priceLong?.let { priceTexts.add("Long Hair: RM $it") }
                        service.priceAll?.let { priceTexts.add("All Length: RM $it") }

                        if (priceTexts.isEmpty()) {
                            Text("Price not available")
                        } else {
                            priceTexts.forEach { Text(it) }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Proceed button
        Button(
            onClick = {
                selectedServiceId?.let { serviceId ->
                    navController.navigate(
                        "selectStylist/$serviceId/$selectedDate/$selectedTimeSlot"
                    )
                }
            },
            enabled = selectedServiceId != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Proceed")
        }
    }
}
