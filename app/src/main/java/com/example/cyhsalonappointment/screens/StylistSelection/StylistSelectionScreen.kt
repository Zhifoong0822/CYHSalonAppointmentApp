package com.example.cyhsalonappointment.screens.StylistSelection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StylistSelectionScreen(
    navController: NavHostController,
    stylistVM: StylistSelectionViewModel,
    serviceId: Int,
    selectedDate: String,
    selectedTimeSlot: String
) {
    // load service from DB
    val service by stylistVM.getService(serviceId).collectAsState(initial = null)



    // load stylists
    val stylists by stylistVM.stylists.collectAsState()

    var selectedHairLength by remember { mutableStateOf<String?>(null) }
    var selectedStylistId by remember { mutableStateOf<String?>(null) }

    // dynamic hair length options based on DB
    val hairLengthOptions = remember(service) {
        mutableListOf<Pair<String, Double>>().apply {
            service?.priceAll?.let { add("All Length" to it) }
            service?.priceShort?.let { add("Short Hair" to it) }
            service?.priceMedium?.let { add("Medium Hair" to it) }
            service?.priceLong?.let { add("Long Hair" to it) }
        }
    }

    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("Select Hair Length and Stylist") },
                navigationIcon = {
                    androidx.compose.material3.IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        androidx.compose.material3.Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // HAIR LENGTH SECTION FIRST
            item {
                Text("Select Hair Length", style = MaterialTheme.typography.titleMedium)
            }

            items(hairLengthOptions) { (label, price) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedHairLength = label
                            selectedStylistId = null // reset stylist when hair length changes
                        },
                    border = if (selectedHairLength == label)
                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    else null
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(label, style = MaterialTheme.typography.bodyLarge)
                        Text("RM $price")
                    }
                }
            }

            // STYLIST LIST ONLY IF HAIR LENGTH SELECTED
            if (selectedHairLength != null) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Select a Stylist", style = MaterialTheme.typography.titleMedium)
                }

                items(stylists) { stylist ->
                    val multiplier = stylistVM.getStylistPriceMultiplier(stylist.stylistLevel)
                    val basePrice = hairLengthOptions.firstOrNull { it.first == selectedHairLength }?.second
                    val previewPrice = basePrice?.times(multiplier)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedStylistId = stylist.stylistID },
                        border = if (selectedStylistId == stylist.stylistID)
                            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                        else null
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(stylist.stylistName, style = MaterialTheme.typography.titleMedium)
                            Text("Level: ${stylist.stylistLevel}")
                            Text("Gender: ${stylist.gender}")
                            previewPrice?.let {
                                Text("Price: RM %.2f".format(it))
                            }
                        }
                    }
                }
            }

            // NEXT BUTTON
            // NEXT BUTTON
            item {
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        // 1. Get the base price of selected hair length
                        val basePrice = hairLengthOptions
                            .first { it.first == selectedHairLength }
                            .second

                        // 2. Get the stylist's level multiplier
                        val multiplier = stylistVM.getStylistPriceMultiplier(
                            stylists.first { it.stylistID == selectedStylistId }.stylistLevel
                        )

                        // 3. Final computed service price
                        val finalPrice = basePrice * multiplier

                        // 4. Generate appointment ID
                        val appointmentId = "APP" +
                                UUID.randomUUID().toString().take(8).uppercase()

                        // 5. Service name from DB
                        val serviceName = service?.serviceName ?: "Service"

                        // 6. Navigate with required parameters
                        navController.navigate(
                            "payment/$appointmentId/$serviceName/${finalPrice.toFloat()}/" +
                                    "$selectedDate/$selectedTimeSlot/$selectedStylistId"
                        )
                    },
                    enabled = selectedStylistId != null && selectedHairLength != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Next: Payment")
                }

                Spacer(modifier = Modifier.height(40.dp))
            }

        }
    }
}

