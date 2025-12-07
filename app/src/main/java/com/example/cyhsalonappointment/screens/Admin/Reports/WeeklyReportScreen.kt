package com.example.cyhsalonappointment.screens.Admin.Reports

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import com.example.cyhsalonappointment.screens.Admin.ReportViewModel
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyReportScreen(viewModel: ReportViewModel, onBack: () -> Unit) {
    val start = LocalDate.now().with(DayOfWeek.MONDAY).toString()
    val end = LocalDate.now().with(DayOfWeek.SUNDAY).toString()
    val result by viewModel.report.collectAsState()

    LaunchedEffect(true) {
        viewModel.loadReport(start, end)
    }

    ReportLayout(
        title = "Weekly Report",
        totalSales = result?.totalSales ?: 0.0,
        totalAppointments = result?.totalAppointments ?: 0,
        average = result?.averagePerAppointment ?: 0.0,
        services = result?.serviceRanking ?: emptyList(),
        onBack = onBack
    )
}
