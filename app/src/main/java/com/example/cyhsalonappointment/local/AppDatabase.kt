package com.example.cyhsalonappointment.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cyhsalonappointment.local.DAO.AppointmentDao
import com.example.cyhsalonappointment.local.DAO.TimeSlotDao
import com.example.cyhsalonappointment.local.entity.TimeSlot
import com.example.cyhsalonappointment.local.entity.Appointment


@Database(
    entities = [TimeSlot::class, Appointment::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timeSlotDao(): TimeSlotDao
    abstract fun appointmentDao(): AppointmentDao
}
