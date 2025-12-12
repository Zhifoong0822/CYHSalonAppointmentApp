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

    val service: SalonService? = services.firstOrNull { it.serviceId == serviceId }

    if (service == null) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text("Service not found")
            Spacer(Modifier.height(12.dp))
            Button(onClick = onBack) { Text("Back") }
        }
        return
    }

    val isSinglePrice = service.priceAll != null

    var priceAll by remember { mutableStateOf(service.priceAll?.toString() ?: "") }
    var priceShort by remember { mutableStateOf(service.priceShort?.toString() ?: "") }
    var priceMedium by remember { mutableStateOf(service.priceMedium?.toString() ?: "") }
    var priceLong by remember { mutableStateOf(service.priceLong?.toString() ?: "") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .size(28.dp)
                .clickable { onBack() }
        )

        Spacer(Modifier.height(12.dp))
        Text("Edit Service", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            readOnly = true,
            value = viewModel.getCategoryNameById(service.categoryId),
            onValueChange = {},
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            readOnly = true,
            value = service.serviceName,
            onValueChange = {},
            label = { Text("Service Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        if (isSinglePrice) {
            OutlinedTextField(
                value = priceAll,
                onValueChange = { priceAll = it },
                label = { Text("Price (RM)") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            OutlinedTextField(
                value = priceShort,
                onValueChange = { priceShort = it },
                label = { Text("Short (RM)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = priceMedium,
                onValueChange = { priceMedium = it },
                label = { Text("Medium (RM)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = priceLong,
                onValueChange = { priceLong = it },
                label = { Text("Long (RM)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = {
                    viewModel.hideService(service) {
                        Toast.makeText(context, "Service removed", Toast.LENGTH_SHORT).show()
                        onBack()
                    }
                }
            ) {
                Text("Remove", color = MaterialTheme.colorScheme.error)
            }

            Button(
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
