package com.example.cyhsalonappointment.screens.Admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceScreen(
    viewModel: ServiceViewModel,
    onBack: () -> Unit
) {
    // ---------------- CATEGORY + SERVICE NAME SETUP ----------------
    val categories = listOf("Hair Cut", "Hair Wash", "Hair Colouring", "Hair Perm")

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

    var selectedCategory by remember { mutableStateOf("") }
    var selectedService by remember { mutableStateOf("") }

    var categoryExpanded by remember { mutableStateOf(false) }
    var serviceExpanded by remember { mutableStateOf(false) }

    var price by remember { mutableStateOf("") }
    var priceShort by remember { mutableStateOf("") }
    var priceMedium by remember { mutableStateOf("") }
    var priceLong by remember { mutableStateOf("") }

    // --- validation (Q3: show error + disable button) ---
    val isSinglePrice = singlePriceList.contains(selectedService)

    val isSinglePriceValid =
        isSinglePrice && price.isNotBlank() && price.toDoubleOrNull() != null

    val isLengthPriceValid =
        !isSinglePrice &&
                priceShort.isNotBlank() && priceShort.toDoubleOrNull() != null &&
                priceMedium.isNotBlank() && priceMedium.toDoubleOrNull() != null &&
                priceLong.isNotBlank() && priceLong.toDoubleOrNull() != null

    val isFormValid =
        selectedCategory.isNotBlank() &&
                selectedService.isNotBlank() &&
                (isSinglePriceValid || isLengthPriceValid)

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Text("Add New Service", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(20.dp))

        // ------------- CATEGORY DROPDOWN (can change anytime) -------------
        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = it }
        ) {
            TextField(
                readOnly = true,
                value = selectedCategory,
                onValueChange = {},
                label = { Text("Service Category") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                categories.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat) },
                        onClick = {
                            selectedCategory = cat
                            // when category changes, allow user to pick service again
                            selectedService = ""
                            categoryExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (selectedCategory.isNotEmpty()) {
            // ------------- SERVICE NAME DROPDOWN (changeable before publish) -------------
            val serviceList = servicesMap[selectedCategory] ?: emptyList()

            ExposedDropdownMenuBox(
                expanded = serviceExpanded,
                onExpandedChange = { serviceExpanded = it }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedService,
                    onValueChange = {},
                    label = { Text("Service Name") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
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

        // ---------------- PRICE INPUT SECTION ----------------
        if (selectedService.isNotEmpty()) {

            if (isSinglePrice) {
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price (RM)") },
                    isError = price.isNotBlank() && price.toDoubleOrNull() == null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (price.isBlank()) {
                    Text("Price is required", color = MaterialTheme.colorScheme.error)
                } else if (price.toDoubleOrNull() == null) {
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
                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = priceMedium,
                    onValueChange = { priceMedium = it },
                    label = { Text("Medium (RM)") },
                    isError = priceMedium.isNotBlank() && priceMedium.toDoubleOrNull() == null,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))

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

            Spacer(Modifier.height(20.dp))

            Button(
                enabled = isFormValid,       // Q3: disable until valid
                onClick = {
                    if (singlePriceList.contains(selectedService)) {
                        viewModel.addSinglePriceService(
                            category = selectedCategory,
                            name = selectedService,
                            price = price
                        )
                    } else {
                        viewModel.addLengthPriceService(
                            category = selectedCategory,
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
