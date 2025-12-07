package com.example.cyhsalonappointment.screens.Admin.Reports

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import com.example.cyhsalonappointment.screens.Admin.ReportViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyReportScreen(viewModel: ReportViewModel, onBack: () -> Unit) {
    val today = LocalDate.now().toString()
    val result by viewModel.report.collectAsState()

    LaunchedEffect(true) {
        viewModel.loadReport(today, today)
    }

    ReportLayout(
        title = "Daily Report",
        totalSales = result?.totalSales ?: 0.0,
        totalAppointments = result?.totalAppointments ?: 0,
        average = result?.averagePerAppointment ?: 0.0,
        services = result?.serviceRanking ?: emptyList(),
        onBack = onBack
    )
}
