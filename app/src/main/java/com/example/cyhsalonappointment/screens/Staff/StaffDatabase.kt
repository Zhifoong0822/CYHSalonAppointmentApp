package com.example.cyhsalonappointment.screens.Staff

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StaffEntity::class], version = 1, exportSchema = false)
abstract class StaffDatabase : RoomDatabase() {

    abstract fun staffDao(): StaffDAO

    companion object {
        @Volatile
        private var INSTANCE: StaffDatabase? = null

        fun getDatabase(context: Context): StaffDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StaffDatabase::class.java,
                    "staff_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}