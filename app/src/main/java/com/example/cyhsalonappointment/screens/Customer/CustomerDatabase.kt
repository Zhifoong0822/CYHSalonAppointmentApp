package com.example.cyhsalonappointment.screens.Customer

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CustomerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CustomerDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDAO

    companion object {
        @Volatile
        private var INSTANCE: CustomerDatabase? = null

        fun getDatabase(context: Context): CustomerDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CustomerDatabase::class.java,
                    "customer_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                Log.d("CustomerDatabase", "Building CustomerDatabase v1 at customer_db")

                INSTANCE = instance
                instance
            }
        }
    }
}