package com.example.cyhsalonappointment

import android.app.Application
import androidx.room.Room
import com.example.cyhsalonappointment.local.AppDatabase
import com.example.cyhsalonappointment.local.entity.Appointment
import com.example.cyhsalonappointment.local.entity.Payment
import com.example.cyhsalonappointment.local.entity.Stylist
import com.example.cyhsalonappointment.local.entity.TimeSlot
import com.example.cyhsalonappointment.screens.Admin.AdminEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
            insertDefaultAdmin()
            val dao = db.timeSlotDao()

            // Check if empty
            if (dao.getAllTimeSlots().isEmpty()) {
                dao.insertTimeSlots(generateDefaultTimeSlots())
            }
            preloadStylists()

            //DEMO DATA FOR PRESENTATION
            preloadDemoAppointmentsAndPayments()




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

    private fun preloadStylists() {
        val dao = AppDatabase.getDatabase(this).stylistDao()
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            val existing = dao.getAllStylists()
            if (existing.isEmpty()) {
                dao.insertStylist(Stylist("ST01", "Alice Tan", "Junior", "Female"))
                dao.insertStylist(Stylist("ST02", "Brandon Lee", "Senior", "Male"))
                dao.insertStylist(Stylist("ST03", "Celine Ng", "Director", "Female"))
            }
        }
    }

    private suspend fun insertDefaultAdmin() {
        val adminDao = db.adminDao()

        // If table is empty, insert one default admin
        if (adminDao.getAllAdmins().isEmpty()) {

            val defaultAdmin = AdminEntity(
                adminId = "A0001",
                password = "admin123",
                name = "CYH"
            )

            adminDao.insertAdmin(defaultAdmin)
        }
    }

    private suspend fun preloadDemoAppointmentsAndPayments() {
        val appointmentDao = db.appointmentDao()
        val paymentDao = db.paymentDao()

        // Prevent duplicates
        if (appointmentDao.countAppointments() > 0) return

        val demoAppointments = listOf(
            // ---------- MONTHLY (NOV 2025) ----------
            Appointment(
                appointmentId = "DEMO_M01",
                appointmentDate = "2025-11-05",
                timeSlotId = "TS0003",
                customerId = "C0001",
                serviceId = 1,
                stylistId = "ST01",
                finalPrice = 80.0,
                serviceName = "Student Hair Wash",
                customerName = "Demo User",
                hairLength = "Short"
            ),
            Appointment(
                appointmentId = "DEMO_M02",
                appointmentDate = "2025-11-18",
                timeSlotId = "TS0006",
                customerId = "C0002",
                serviceId = 1,
                stylistId = "ST02",
                finalPrice = 80.0,
                serviceName = "Student Hair Cut",
                customerName = "Demo User",
                hairLength = "Medium"
            ),

            // ---------- WEEKLY (DEC 16–22) ----------
            Appointment(
                appointmentId = "DEMO_W01",
                appointmentDate = "2025-12-16",
                timeSlotId = "TS0004",
                customerId = "C0003",
                serviceId = 1,
                stylistId = "ST01",
                finalPrice = 80.0,
                serviceName = "Student Hair Cut",
                customerName = "Demo User",
                hairLength = "Short"
            ),
            Appointment(
                appointmentId = "DEMO_W02",
                appointmentDate = "2025-12-20",
                timeSlotId = "TS0007",
                customerId = "C0004",
                serviceId = 1,
                stylistId = "ST03",
                finalPrice = 80.0,
                serviceName = "Student Hair Wash",
                customerName = "Demo User",
                hairLength = "Long"
            ),

            // ---------- DAILY (DEC 22) ----------
            Appointment(
                appointmentId = "DEMO_D01",
                appointmentDate = "2025-12-22",
                timeSlotId = "TS0005",
                customerId = "C0005",
                serviceId = 1,
                stylistId = "ST02",
                finalPrice = 80.0,
                serviceName = "Student Hair Cut",
                customerName = "Demo User",
                hairLength = "Medium"
            )
        )

        demoAppointments.forEach { appointment ->
            appointmentDao.insertAppointment(appointment)

            paymentDao.insertPayment(
                Payment(
                    paymentId = "PAY_${appointment.appointmentId}",
                    appointmentId = appointment.appointmentId,
                    purchaseAmount = 80.0,
                    bookingFee = 10.0,
                    taxAmount = 5.4,
                    totalAmount = 95.4,
                    paymentMethod = "Credit/Debit",
                    paymentDate = appointment.appointmentDate,
                    paymentTime = "14:00",
                    status = "Successful"
                )
            )
        }
    }
}
