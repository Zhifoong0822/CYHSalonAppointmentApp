package com.example.cyhsalonappointment.local.entity
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "Appointment",
    indices = [
        Index(
            value = ["appointmentDate", "timeSlotId", "stylistId"],
            unique = true
        )
    ]
)data class Appointment(
    @PrimaryKey
    val appointmentId: String,   // e.g., "APP0001"
    val appointmentDate: String, // e.g., "2025-11-26"
    val timeSlotId: String,      // FK to TimeSlot, e.g., "TS0001"
    val customerId: String?,     // FK to Customer, e.g., "C0001"
    val serviceId: Int ,     // FK to Service, e.g., "SV0001"
    val stylistId: String?,
    val isCancelled: Boolean = false,

    val finalPrice: Double? = null,
    val serviceName: String? = null,
    val customerName: String? = null,
    val hairLength: String? = null   // "Short" | "Medium" | "Long"

)
