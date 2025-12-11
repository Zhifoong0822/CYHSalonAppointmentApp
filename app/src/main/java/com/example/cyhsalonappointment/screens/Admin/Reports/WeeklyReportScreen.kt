package com.example.cyhsalonappointment.screens.Admin.Reports

import android.os.Build
import android.widget.GridLayout
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.cyhsalonappointment.screens.Admin.ReportViewModel
import java.time.LocalDate
import java.time.DayOfWeek
import androidx.compose.ui.Alignment

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyReportScreen(viewModel: ReportViewModel, onBack: () -> Unit) {
    val today = LocalDate.now()
    val start = today.with(DayOfWeek.MONDAY)
    val end = today.with(DayOfWeek.SUNDAY)

    val result by viewModel.report.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadReport(start.toString(), end.toString())
    }

    if (result == null) {
        LoadingReportUI()
    } else {
        ReportLayout(
            title = "Weekly Report",
            totalSales = result!!.totalSales,
            totalAppointments = result!!.totalAppointments,
            average = result!!.averagePerAppointment,
            services = result!!.serviceRanking,
            onBack = onBack
        )
    }

}
@Composable
fun LoadingReportUI() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
