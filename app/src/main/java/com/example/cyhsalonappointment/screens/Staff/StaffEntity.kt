package com.example.cyhsalonappointment.screens.Staff

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "staff")
data class StaffEntity(
    @PrimaryKey val staffId: String,
    val name: String,
    val password: String
)