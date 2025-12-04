package com.example.cyhsalonappointment.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TimeSlot")
data class TimeSlot(
    @PrimaryKey
    val timeSlotId: String,   // e.g., "TS0001"
    val timeSlot: String      // e.g., "10:00"
)


