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
    val today = LocalDate.now()
    val start = today.minusMonths(1).withDayOfMonth(1)
    val end = today.minusMonths(1).withDayOfMonth(
        today.minusMonths(1).lengthOfMonth()
    )

    val result by viewModel.report.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadReport(
            start.toString(),
            end.toString()
        )
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
