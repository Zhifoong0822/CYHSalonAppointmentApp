package com.example.cyhsalonappointment.screens.Admin

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.cyhsalonappointment.local.entity.SalonService


@Composable
fun EditServiceScreen(
    serviceId: Int,
    viewModel: ServiceViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val services by viewModel.services.collectAsState()

    // Find the service by ID from ViewModel
    val service: SalonService? = services.firstOrNull { it.serviceId == serviceId }

    // If not found, do NOT crash – just show message
    if (service == null) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text("Service not found", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onBack) {
                Text("Back")
            }
        }
        return
    }

    // determine type by existing data
    val isSinglePrice = service.priceAll != null

    var priceAll by remember { mutableStateOf(service.priceAll?.toString() ?: "") }
    var priceShort by remember { mutableStateOf(service.priceShort?.toString() ?: "") }
    var priceMedium by remember { mutableStateOf(service.priceMedium?.toString() ?: "") }
    var priceLong by remember { mutableStateOf(service.priceLong?.toString() ?: "") }

    val isSinglePriceValid =
        isSinglePrice && priceAll.isNotBlank() && priceAll.toDoubleOrNull() != null

    val isLengthPriceValid =
        !isSinglePrice &&
                priceShort.isNotBlank() && priceShort.toDoubleOrNull() != null &&
                priceMedium.isNotBlank() && priceMedium.toDoubleOrNull() != null &&
                priceLong.isNotBlank() && priceLong.toDoubleOrNull() != null

    val isFormValid = isSinglePriceValid || isLengthPriceValid

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .size(28.dp)
                .clickable { onBack() }
        )

        Spacer(Modifier.height(12.dp))

        Text("My Services", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text("Edit Service", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))

        // Category (read-only)
        OutlinedTextField(
            readOnly = true,
            value = viewModel.getCategoryNameById(service.categoryId),
            onValueChange = {},
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        // Name (read-only)
        OutlinedTextField(
            readOnly = true,
            value = service.serviceName,
            onValueChange = {},
            label = { Text("Service Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(25.dp))

        if (isSinglePrice) {
            OutlinedTextField(
                value = priceAll,
                onValueChange = { priceAll = it },
                label = { Text("Price (RM)") },
                isError = priceAll.isNotBlank() && priceAll.toDoubleOrNull() == null,
                modifier = Modifier.fillMaxWidth()
            )

            when {
                priceAll.isBlank() ->
                    Text("Price is required", color = MaterialTheme.colorScheme.error)
                priceAll.toDoubleOrNull() == null ->
                    Text("Enter a valid number", color = MaterialTheme.colorScheme.error)
            }
        } else {
            OutlinedTextField(
                value = priceShort,
                onValueChange = { priceShort = it },
                label = { Text("Short (RM)") },
                isError = priceShort.isNotBlank() && priceShort.toDoubleOrNull() == null,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = priceMedium,
                onValueChange = { priceMedium = it },
                label = { Text("Medium (RM)") },
                isError = priceMedium.isNotBlank() && priceMedium.toDoubleOrNull() == null,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = priceLong,
                onValueChange = { priceLong = it },
                label = { Text("Long (RM)") },
                isError = priceLong.isNotBlank() && priceLong.toDoubleOrNull() == null,
                modifier = Modifier.fillMaxWidth()
            )

            if (!isLengthPriceValid) {
                Text(
                    "All three prices are required and must be valid numbers",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = {
                    viewModel.hideService(service) {
                        Toast.makeText(
                            context,
                            "Service removed from list",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBack()
                    }
                }
            ) {
                Text("Remove", color = MaterialTheme.colorScheme.error)
            }

            Button(
                enabled = isFormValid,
                onClick = {
                    val updated = if (isSinglePrice) {
                        service.copy(
                            priceAll = priceAll.toDouble(),
                            priceShort = null,
                            priceMedium = null,
                            priceLong = null
                        )
                    } else {
                        service.copy(
                            priceAll = null,
                            priceShort = priceShort.toDouble(),
                            priceMedium = priceMedium.toDouble(),
                            priceLong = priceLong.toDouble()
                        )
                    }

                    viewModel.updateService(updated)
                    Toast.makeText(context, "Service updated", Toast.LENGTH_SHORT).show()
                    onBack()
                }
            ) {
                Text("Save Changes")
            }
        }
    }
}
