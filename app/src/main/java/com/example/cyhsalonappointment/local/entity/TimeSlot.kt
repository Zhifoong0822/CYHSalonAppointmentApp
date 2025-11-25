package com.example.cyhsalonappointment.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TimeSlot")
data class TimeSlot(
    @PrimaryKey(autoGenerate = true)
    val timeSlotId: Int = 0,
    val timeSlot: String
)
