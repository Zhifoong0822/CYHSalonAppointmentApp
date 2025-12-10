
package com.example.cyhsalonappointment.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "Payment")
data class Payment(
    @PrimaryKey
    val paymentId: String,           // e.g., "PAY0001"
    val appointmentId: String,       // FK to Appointment
    val purchaseAmount: Double,      // Service price (e.g., 50.00)
    val bookingFee: Double,          // Booking fee (e.g., 10.00)
    val taxAmount: Double = 0.0,     // Tax (e.g., 3.00)
    val totalAmount: Double,         // Total = purchase + booking + tax
    val paymentMethod: String,       // "Credit/Debit", "TNG", "PayPal"
    val paymentDate: String,         // e.g., "2025-12-14"
    val paymentTime: String,         // e.g., "16:55"
    val status: String = "Successful" // "PENDING", "COMPLETED", "FAILED"
)
