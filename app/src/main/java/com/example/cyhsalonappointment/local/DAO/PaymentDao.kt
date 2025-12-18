
package com.example.cyhsalonappointment.local.DAO

import androidx.room.*
import com.example.cyhsalonappointment.local.entity.Payment
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {

    @Insert
    suspend fun insertPayment(payment: Payment)

    @Query("SELECT * FROM Payment WHERE appointmentId = :appointmentId")
    suspend fun getPaymentByAppointment(appointmentId: String): Payment?

    @Query("SELECT * FROM Payment ORDER BY paymentDate DESC, paymentTime DESC")
    fun getAllPayments(): Flow<List<Payment>>

    @Query("SELECT * FROM Payment WHERE paymentId = :id")
    suspend fun getPaymentById(id: String): Payment?

    @Query("SELECT COUNT(*) FROM Payment")
    suspend fun getPaymentCount(): Int

    @Query("""
        SELECT * FROM Payment 
        WHERE paymentDate = :date 
        ORDER BY paymentTime DESC
    """)
    suspend fun getPaymentsByDate(date: String): List<Payment>


    @Query("""
        SELECT SUM(totalAmount) FROM Payment 
        WHERE status = 'Successful'
    """)
    suspend fun getTotalRevenue(): Double
    @Query("""
    SELECT Payment.*
    FROM Payment
    INNER JOIN Appointment
        ON Payment.appointmentId = Appointment.appointmentId
    WHERE Appointment.customerId = :customerId
    ORDER BY Payment.paymentDate DESC
""")
    suspend fun getPaymentsForCustomer(customerId: String): List<Payment>


}
