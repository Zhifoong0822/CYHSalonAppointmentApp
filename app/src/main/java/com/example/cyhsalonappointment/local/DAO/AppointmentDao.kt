package com.example.cyhsalonappointment.local.DAO

import androidx.room.*
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

    @Query("UPDATE Appointment SET isCancelled = 1 WHERE appointmentId = :id")
    suspend fun cancelAppointment(id: String)

    @Query("""
        UPDATE Appointment
        SET appointmentDate = :newDate, timeSlotId = :newTimeSlotId
        WHERE appointmentId = :appointmentId
    """)
    suspend fun updateAppointment(
        appointmentId: String,
        newDate: String,
        newTimeSlotId: String
    )

    @Query("""
        UPDATE Appointment
        SET finalPrice = :finalPrice,
            serviceName = :serviceName,
            customerName = :customerName
        WHERE appointmentId = :appointmentId
    """)
    suspend fun attachDetails(
        appointmentId: String,
        finalPrice: Double?,
        serviceName: String?,
        customerName: String?
    )

    @Query("""
    UPDATE Appointment
    SET finalPrice = :finalPrice,
        serviceName = :serviceName,
        customerName = :customerName,
        hairLength = :hairLength
    WHERE appointmentId = :appointmentId
""")
    suspend fun attachDetailsOnCompletion(
        appointmentId: String,
        finalPrice: Double,
        serviceName: String,
        customerName: String,
        hairLength: String
    )

    @Query("SELECT * FROM appointment WHERE customerId = :customerId")
    suspend fun getAppointmentsForUser(customerId: String): List<Appointment>

    @Query("SELECT COUNT(*) FROM Appointment")
    suspend fun countAppointments(): Int



}
