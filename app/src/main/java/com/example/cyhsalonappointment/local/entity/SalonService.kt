package com.example.cyhsalonappointment.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class SalonService(
    @PrimaryKey(autoGenerate = true)
    val serviceId: Int = 0,
    val categoryId: Int,
    val serviceName: String,
    // if a service uses single price (eg. haircut), priceAll will hold the price
    val priceAll: Double?,
    val priceShort: Double? = null,
    val priceMedium: Double? = null,
    val priceLong: Double? = null,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
