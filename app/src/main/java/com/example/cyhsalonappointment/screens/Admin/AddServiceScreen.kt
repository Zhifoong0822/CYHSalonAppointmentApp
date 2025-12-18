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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceScreen(
    viewModel: ServiceViewModel,
    onBack: () -> Unit
) {
    // FIXED category list (ID + label)
    val categories = listOf(
        1 to "Hair Cut",
        2 to "Hair Wash",
        3 to "Hair Colouring",
        4 to "Hair Perm"
    )

    val servicesMap = mapOf(
        "Hair Cut" to listOf("Student Hair Cut", "Men Hair Cut", "Women Hair Cut"),
        "Hair Wash" to listOf("Student Hair Wash", "Men Hair Wash", "Women Hair Wash"),
        "Hair Colouring" to listOf("Touch Up", "Full Hair Colour", "Highlight", "Balayage", "Airtouch"),
        "Hair Perm" to listOf("Men Perm", "Cold Perm", "Digital Perm", "Air Wave Perm", "Root Perm")
    )

    val singlePriceList = listOf(
        "Student Hair Cut", "Men Hair Cut", "Women Hair Cut",
        "Men Hair Wash", "Touch Up", "Root Perm"
    )

    var categoryExpanded by remember { mutableStateOf(false) }
    var serviceExpanded by remember { mutableStateOf(false) }

    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    var selectedCategoryName by remember { mutableStateOf("") }
    var selectedService by remember { mutableStateOf("") }

    var price by remember { mutableStateOf("") }
    var priceShort by remember { mutableStateOf("") }
    var priceMedium by remember { mutableStateOf("") }
    var priceLong by remember { mutableStateOf("") }

    val isSinglePrice = singlePriceList.contains(selectedService)

    val isSinglePriceValid =
        isSinglePrice && price.isNotBlank() && price.toDoubleOrNull() != null

    val isLengthPriceValid =
        !isSinglePrice &&
                priceShort.toDoubleOrNull() != null &&
                priceMedium.toDoubleOrNull() != null &&
                priceLong.toDoubleOrNull() != null

    val isFormValid =
        selectedCategoryId != null &&
                selectedService.isNotBlank() &&
                (isSinglePriceValid || isLengthPriceValid)

    val error by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .size(28.dp)
                .clickable { onBack() }
        )

        Spacer(Modifier.height(12.dp))
        Text("Add New Service", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(20.dp))

        // CATEGORY DROPDOWN
        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = it }
        ) {
            TextField(
                readOnly = true,
                value = selectedCategoryName,
                onValueChange = {},
                label = { Text("Service Category") },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                categories.forEach { (id, name) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            selectedCategoryId = id
                            selectedCategoryName = name
                            selectedService = ""
                            categoryExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (selectedCategoryName.isNotEmpty()) {
            val serviceList = servicesMap[selectedCategoryName] ?: emptyList()

            ExposedDropdownMenuBox(
                expanded = serviceExpanded,
                onExpandedChange = { serviceExpanded = it }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedService,
                    onValueChange = {},
                    label = { Text("Service Name") },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = serviceExpanded,
                    onDismissRequest = { serviceExpanded = false }
                ) {
                    serviceList.forEach { svc ->
                        DropdownMenuItem(
                            text = { Text(svc) },
                            onClick = {
                                selectedService = svc
                                serviceExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        if (selectedService.isNotEmpty()) {

            if (isSinglePrice) {
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
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

            Spacer(Modifier.height(20.dp))

            Button(
                enabled = isFormValid,
                onClick = {
                    if (isSinglePrice) {
                        viewModel.addSinglePriceService(
                            categoryId = selectedCategoryId!!,
                            name = selectedService,
                            price = price
                        )
                    } else {
                        viewModel.addLengthPriceService(
                            categoryId = selectedCategoryId!!,
                            name = selectedService,
                            short = priceShort,
                            medium = priceMedium,
                            long = priceLong
                        )
                    }
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publish")
            }
        }
    }
}
