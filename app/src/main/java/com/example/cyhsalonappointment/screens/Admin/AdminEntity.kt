package com.example.cyhsalonappointment.screens.Admin

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin")
data class AdminEntity(
    @PrimaryKey val adminId: String,
    val name: String,
    val password: String
)