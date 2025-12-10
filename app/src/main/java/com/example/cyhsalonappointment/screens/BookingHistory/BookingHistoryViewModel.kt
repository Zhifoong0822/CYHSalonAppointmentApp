package com.example.cyhsalonappointment.screens.BookingHistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.cyhsalonappointment.local.DAO.AppointmentDao
import com.example.cyhsalonappointment.local.DAO.ServiceDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

class BookingHistoryViewModel(
    private val appointmentDao: AppointmentDao,
    private val serviceDao: ServiceDao
) : ViewModel() {

    private val _appointments = MutableLiveData<List<AppointmentDisplay>>()
    val appointments: LiveData<List<AppointmentDisplay>> = _appointments

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadAppointments(isAdmin: Boolean = false, status: String? = null) {
        viewModelScope.launch {
            val list = if (isAdmin) appointmentDao.getAllAppointments()
            else emptyList() // Use actual customer ID if needed

            val today = LocalDate.now()
            val displayList = list.map { appt ->
                val apptDate = LocalDate.parse(appt.appointmentDate)
                val statusText = when {
                    appt.isCancelled -> "Cancelled"
                    apptDate.isBefore(today) -> "Completed"
                    apptDate.isEqual(today) -> "Today"
                    else -> "Upcoming"
                }
                val service = serviceDao.getServiceById(appt.serviceId).first()
                AppointmentDisplay(
                    appointmentId = appt.appointmentId,
                    serviceName = service?.serviceName ?: "Unknown",
                    date = appt.appointmentDate,
                    timeSlotId = appt.timeSlotId,
                    status = statusText,
                    stylistName = appt.stylistId,
                    hairLength = appt.hairLength,
                    price = appt.finalPrice
                )
            }

            _appointments.value = if (!status.isNullOrEmpty())
                displayList.filter { it.status == status }
            else displayList
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun cancelBooking(id: String) {
        viewModelScope.launch {
            appointmentDao.cancelAppointment(id)
            loadAppointments() // reload after cancellation
        }
    }
}
