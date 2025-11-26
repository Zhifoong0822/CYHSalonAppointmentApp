package com.example.cyhsalonappointment

import android.app.Application
import androidx.room.Room
import com.example.cyhsalonappointment.local.AppDatabase
import com.example.cyhsalonappointment.local.entity.TimeSlot
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class App : Application() {

    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "salon.db"
        ).build()

        GlobalScope.launch {
            val dao = db.timeSlotDao()

            // Check if empty
            if (dao.getAllTimeSlots().isEmpty()) {
                dao.insertTimeSlots(generateDefaultTimeSlots())
            }
        }

    }

    //generate fixed time slot from 10-6pm if empty
    private fun generateDefaultTimeSlots(): List<TimeSlot> {
        val slots = mutableListOf<TimeSlot>()
        var hour = 10
        var minute = 0
        var counter = 1

        while (hour < 18 || (hour == 18 && minute == 0)) {
            val time = String.format("%02d:%02d", hour, minute)
            val id = "TS%04d".format(counter)   // TS0001, TS0002, etc.
            slots.add(TimeSlot(timeSlotId = id, timeSlot = time))

            minute += 30
            if (minute == 60) {
                minute = 0
                hour++
            }
            counter++
        }

        return slots
    }


}
