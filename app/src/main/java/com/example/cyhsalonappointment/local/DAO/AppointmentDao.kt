package com.example.cyhsalonappointment.local.DAO

import androidx.room.*
import androidx.room.Insert
import com.example.cyhsalonappointment.local.entity.Appointment

@Dao
interface AppointmentDao {

    @Insert
    suspend fun insertAppointment(appointment: Appointment)

    @Query("SELECT * FROM Appointment")
    suspend fun getAllAppointments(): List<Appointment>

    @Query("SELECT * FROM Appointment WHERE appointmentId = :id")
    suspend fun getAppointmentById(id: String): Appointment?

    @Query("SELECT COUNT(*) FROM Appointment")
    suspend fun getAppointmentCount(): Int
}
