package com.example.cyhsalonappointment.local.DAO

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ReportDAO {

    // TOTAL SALES = SUM of purchaseAmount in Payment table
    @Query("""
    SELECT SUM(P.purchaseAmount)
    FROM Payment P
    JOIN Appointment A ON P.appointmentId = A.appointmentId
    WHERE substr(A.appointmentDate, 1, 10)
 BETWEEN :start AND :end
      AND P.status = 'Successful'
""")
    suspend fun getTotalSales(start: String, end: String): Double?


    // COUNT completed APPOINTMENTS
    // Completed means: NOT cancelled AND has a payment record
    @Query("""
        SELECT COUNT(*) 
        FROM Appointment JOIN Payment P
        WHERE date(appointmentDate) BETWEEN :start AND :end
          AND isCancelled = 0
          AND P.status = 'Successful'
    """)
    suspend fun getTotalCompletedAppointments(start: String, end: String): Int

    //get top customer spent
    @Query("""
    SELECT A.customerId AS customerId,
           COUNT(*) AS totalVisits,
           SUM(P.purchaseAmount) AS totalSpent
    FROM Payment P
    JOIN Appointment A ON P.appointmentId = A.appointmentId
    WHERE substr(A.appointmentDate, 1, 10)
 BETWEEN :start AND :end
      AND P.status = 'Successful'
    GROUP BY A.customerId
    ORDER BY totalSpent DESC
""")
    suspend fun getTopCustomers(start: String, end: String): List<CustomerReport>

    // TOP SELLING SERVICES (based on Payment + Appointment join)
    @Query("""
    SELECT 
        A.serviceName AS serviceName,
        A.appointmentDate AS date,
        SUM(P.purchaseAmount) AS total
    FROM Payment P
    JOIN Appointment A ON P.appointmentId = A.appointmentId
    WHERE substr(A.appointmentDate, 1, 10) BETWEEN :start AND :end
      AND P.status = 'Successful'
      AND A.serviceName IS NOT NULL
    GROUP BY A.serviceName, P.paymentDate
    ORDER BY A.appointmentDate DESC
""")
    suspend fun getTopServiceSales(
        start: String,
        end: String
    ): List<ServiceSalesReport>


}


data class ServiceSalesReport(
    val serviceName: String,
    val date: String,
    val total: Double
)

data class CustomerReport(
    val customerId: String,
    val totalVisits: Int,
    val totalSpent: Double
)

