package com.example.cyhsalonappointment.local.DAO

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ReportDAO {

    @Query("""
        SELECT SUM(finalPrice) 
        FROM Appointment
        WHERE appointmentDate BETWEEN :start AND :end
        AND isCancelled = 0
    """)
    suspend fun getTotalSales(start: String, end: String): Double?

    @Query("""
        SELECT COUNT(*)
        FROM Appointment
        WHERE appointmentDate BETWEEN :start AND :end
        AND isCancelled = 0
    """)
    suspend fun getTotalCompletedAppointments(start: String, end: String): Int

    @Query("""
        SELECT serviceName, SUM(finalPrice) as total
        FROM Appointment
        WHERE appointmentDate BETWEEN :start AND :end
        AND isCancelled = 0
        GROUP BY serviceName
        ORDER BY total DESC
    """)
    suspend fun getTopServiceSales(start: String, end: String): List<ServiceSalesReport>

    @Query("""
        SELECT customerName, SUM(finalPrice) as spent
        FROM Appointment
        WHERE appointmentDate BETWEEN :start AND :end
        AND isCancelled = 0
        GROUP BY customerName
        ORDER BY spent DESC
    """)
    suspend fun getTopCustomers(start: String, end: String): List<CustomerReport>
}

data class ServiceSalesReport(
    val serviceName: String,
    val total: Double
)

data class CustomerReport(
    val customerName: String,
    val spent: Double
)
