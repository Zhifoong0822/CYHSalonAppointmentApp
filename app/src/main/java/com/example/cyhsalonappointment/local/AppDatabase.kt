package com.example.cyhsalonappointment.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cyhsalonappointment.local.DAO.AppointmentDao
import com.example.cyhsalonappointment.local.DAO.PaymentDao
import com.example.cyhsalonappointment.local.DAO.ServiceDao
import com.example.cyhsalonappointment.local.DAO.StylistDao
import com.example.cyhsalonappointment.local.DAO.TimeSlotDao
import com.example.cyhsalonappointment.local.entity.TimeSlot
import com.example.cyhsalonappointment.local.entity.Appointment
import com.example.cyhsalonappointment.screens.Admin.AdminDAO
import com.example.cyhsalonappointment.screens.Admin.AdminEntity
import com.example.cyhsalonappointment.screens.Customer.CustomerDAO
import com.example.cyhsalonappointment.screens.Customer.CustomerEntity
import com.example.cyhsalonappointment.local.entity.Stylist
import com.example.cyhsalonappointment.local.entity.SalonService
import com.example.cyhsalonappointment.local.DAO.ReportDAO
import com.example.cyhsalonappointment.local.entity.Payment


@Database(
    entities = [
        TimeSlot::class,
        Appointment::class,
        Stylist::class,
        CustomerEntity::class,
        AdminEntity::class,
        SalonService::class,
        Payment::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timeSlotDao(): TimeSlotDao
    abstract fun appointmentDao(): AppointmentDao

    abstract fun stylistDao(): StylistDao
    abstract fun customerDao(): CustomerDAO
    abstract fun adminDao(): AdminDAO

    abstract fun serviceDao(): ServiceDao

    abstract fun reportDao(): ReportDAO

    abstract fun paymentDao(): PaymentDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "salon.db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
