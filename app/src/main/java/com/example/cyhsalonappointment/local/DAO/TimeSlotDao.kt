package com.example.cyhsalonappointment.local.DAO

import androidx.room.*
import com.example.cyhsalonappointment.local.entity.TimeSlot
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeSlotDao {

    @Insert
    suspend fun insertTimeSlots(timeSlots: List<TimeSlot>)

    @Query("SELECT * FROM TimeSlot")
    suspend fun getAllTimeSlots(): List<TimeSlot>

    @Query("SELECT * FROM TimeSlot")
    fun getAllTimeSlotsFlow(): Flow<List<TimeSlot>>
}
