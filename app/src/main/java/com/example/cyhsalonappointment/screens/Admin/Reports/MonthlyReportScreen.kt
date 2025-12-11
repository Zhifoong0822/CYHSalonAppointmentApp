package com.example.cyhsalonappointment.screens.Admin.Reports

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import com.example.cyhsalonappointment.screens.Admin.ReportViewModel
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthlyReportScreen(viewModel: ReportViewModel, onBack: () -> Unit) {
    val start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).toString()
    val end = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).toString()
    val result by viewModel.report.collectAsState()

    LaunchedEffect(true) {
        viewModel.loadReport(start, end)
    }
    if (result == null) {
        LoadingReportUI()
    } else {
        ReportLayout(
            title = "Monthly Report",
            totalSales = result?.totalSales ?: 0.0,
            totalAppointments = result?.totalAppointments ?: 0,
            average = result?.averagePerAppointment ?: 0.0,
            services = result?.serviceRanking ?: emptyList(),
            onBack = onBack
        )
    }

}
