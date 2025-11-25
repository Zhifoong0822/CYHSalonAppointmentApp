package com.example.cyhsalonappointment.screens.Customer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey val customerId: String,
    val username: String,
    val gender: String,
    val email: String,
    val contactNumber: String,
    val password: String,
    val createdAt: Long,
    val updatedAt: Long
)