package com.example.cyhsalonappointment

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.cyhsalonappointment.AppointmentReminderReceiver

class AppointmentReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val message = inputData.getString("message") ?: "Appointment Reminder"
        val appointmentId = inputData.getString("appointmentId") ?: ""

        // Use your existing BroadcastReceiver to show notification
        val intent = Intent(applicationContext, AppointmentReminderReceiver::class.java).apply {
            putExtra("message", message)
            putExtra("appointmentId", appointmentId)
        }
        applicationContext.sendBroadcast(intent)

        return Result.success()
    }
}