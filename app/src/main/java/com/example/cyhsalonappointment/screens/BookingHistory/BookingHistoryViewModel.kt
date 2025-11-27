package com.example.cyhsalonappointment.screens.BookingHistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyhsalonappointment.local.DAO.AppointmentDao
import kotlinx.coroutines.launch
import java.time.LocalDate

    class BookingHistoryViewModel(private val dao: AppointmentDao) : ViewModel() {

    private val _appointments = MutableLiveData<List<AppointmentDisplay>>()
    val appointments: LiveData<List<AppointmentDisplay>> = _appointments

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadAppointments() {
        viewModelScope.launch {
            val list = dao.getAllAppointments()
            val today = LocalDate.now()

            val displayList = list.map { appt ->
                val apptDate = LocalDate.parse(appt.appointmentDate)
                val status = when {
                    apptDate.isBefore(today) -> "Completed"
                    apptDate.isEqual(today) -> "Today"
                    else -> "Upcoming"
                }

                AppointmentDisplay(
                    appointmentId = appt.appointmentId,
                    date = appt.appointmentDate,
                    timeSlotId = appt.timeSlotId,
                    status = status
                )
            }

            _appointments.value = displayList
        }
    }
}
