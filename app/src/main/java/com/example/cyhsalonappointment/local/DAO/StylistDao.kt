package com.example.cyhsalonappointment.local.DAO

import androidx.room.*
import com.example.cyhsalonappointment.local.entity.Stylist

@Dao
interface StylistDao {

    @Query("SELECT * FROM stylists")
    suspend fun getAllStylists(): List<Stylist>

    @Query("SELECT * FROM stylists WHERE stylistID = :id")
    suspend fun getStylistById(id: String): Stylist?

    @Insert
    suspend fun insertStylist(stylist: Stylist)

    @Update
    suspend fun updateStylist(stylist: Stylist)

    @Delete
    suspend fun deleteStylist(stylist: Stylist)

    @Query("""
    SELECT * FROM stylists
    WHERE stylistID NOT IN (
        SELECT stylistId FROM Appointment
        WHERE appointmentDate = :date
          AND timeSlotId = :timeSlotId
          AND isCancelled = 0
    )
""")
    suspend fun getAvailableStylists(
        date: String,
        timeSlotId: String
    ): List<Stylist>

}
