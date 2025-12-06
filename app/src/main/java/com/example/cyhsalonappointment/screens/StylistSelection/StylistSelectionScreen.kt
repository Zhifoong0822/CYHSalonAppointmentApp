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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cyhsalonappointment.local.entity.Stylist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StylistSelectionScreen(
    navController: NavHostController,
    stylistVM: StylistSelectionViewModel,
    serviceName: String,
    selectedDate: String,
    selectedTimeSlot: String
) {
    val stylists by stylistVM.stylists.collectAsState()
    var selectedStylistId by remember { mutableStateOf<String?>(null) }

    val hairLengthOptions = listOf(
        "Short Hair" to 10,
        "Medium Hair" to 20,
        "Long Hair" to 30
    )
    var selectedHairLength by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("Select Stylist") },
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

            // TITLE
            item {
                Text(
                    text = "Select a Stylist",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // STYLIST LIST
            items(stylists) { stylist ->
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
                    }
                }
            }

            // HAIR LENGTH SECTION
            item {
                Text(
                    "Select Hair Length",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(hairLengthOptions) { (label, price) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedHairLength = label },
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

            // NEXT BUTTON
            item {
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        navController.navigate(
                            "tempPayment/$serviceName/$selectedDate/$selectedTimeSlot/$selectedStylistId/$selectedHairLength"
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






