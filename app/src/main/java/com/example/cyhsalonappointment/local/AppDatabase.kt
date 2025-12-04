package com.example.cyhsalonappointment.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cyhsalonappointment.local.DAO.AppointmentDao
import com.example.cyhsalonappointment.local.DAO.StylistDao
import com.example.cyhsalonappointment.local.DAO.TimeSlotDao
import com.example.cyhsalonappointment.local.entity.TimeSlot
import com.example.cyhsalonappointment.local.entity.Appointment
import com.example.cyhsalonappointment.local.entity.Stylist


@Database(
    entities = [TimeSlot::class, Appointment::class, Stylist::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timeSlotDao(): TimeSlotDao
    abstract fun appointmentDao(): AppointmentDao

    abstract fun stylistDao(): StylistDao
}
