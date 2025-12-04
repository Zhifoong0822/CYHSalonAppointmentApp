package com.example.cyhsalonappointment.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stylists")
data class Stylist(
    @PrimaryKey val stylistID: String,
    val stylistName: String,
    val stylistLevel: String,
    val gender: String
)
