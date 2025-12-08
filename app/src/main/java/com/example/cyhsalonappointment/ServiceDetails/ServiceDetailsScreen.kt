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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cyhsalonappointment.local.entity.SalonService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailsScreen(
    navController: NavHostController,
    categoryId: Int,
    selectedDate: String,
    selectedTimeSlot: String,
    viewModel: ServiceDetailViewModel = viewModel()
){
    var selectedServiceId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(categoryId) {
        viewModel.loadServicesByCategory(categoryId)
    }

    // Collect services from ViewModel
    val services by viewModel.services.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Select Service Details", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (services.isEmpty()) {
            Text("No service details available.", style = MaterialTheme.typography.bodyLarge)
        } else {
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
                        service.priceShort?.let { Text("Short: RM $it") }
                        service.priceMedium?.let { Text("Medium: RM $it") }
                        service.priceLong?.let { Text("Long: RM $it") }
                        service.priceAll?.let { Text("All Length: RM $it") }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

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

