package com.example.cyhsalonappointment.screens.Admin

import com.example.cyhsalonappointment.local.DAO.CustomerReport
import com.example.cyhsalonappointment.local.DAO.ReportDAO
import com.example.cyhsalonappointment.local.DAO.ServiceSalesReport



class ReportRepository(private val dao: ReportDAO) {

    suspend fun getReport(start: String, end: String): ReportResult {
        val total = dao.getTotalSales(start, end) ?: 0.0
        val count = dao.getTotalCompletedAppointments(start, end)
        val avg = if (count > 0) total / count else 0.0

        return ReportResult(
            totalSales = total,
            totalAppointments = count,
            averagePerAppointment = avg,
            serviceRanking = dao.getTopServiceSales(start, end),
            customerRanking = dao.getTopCustomers(start, end)
        )
    }
}

data class ReportResult(
    val totalSales: Double,
    val totalAppointments: Int,
    val averagePerAppointment: Double,
    val serviceRanking: List<ServiceSalesReport>,
    val customerRanking: List<CustomerReport>
)
