package com.example.cyhsalonappointment.screens.Admin.Reports

import androidx.compose.runtime.*
import com.example.cyhsalonappointment.screens.Admin.ReportViewModel

@Composable
fun CustomerReportScreen(viewModel: ReportViewModel, onBack: () -> Unit) {
    val result by viewModel.report.collectAsState()

    LaunchedEffect(true) {
        viewModel.loadReport("0000-01-01", "9999-12-31") // full history
    }

    CustomerReportLayout(
        title = "Top Customers",
        customers = result?.customerRanking ?: emptyList(),
        onBack = onBack
    )
}
