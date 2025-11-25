package com.example.cyhsalonappointment.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Appointment")
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val appointmentId: Int = 0,

    val appointmentDate: String,
    val timeSlotId: Int,

    // later foreign key, for now optional
    val customerId: Int? = null,
    val serviceId: Int? = null
)
