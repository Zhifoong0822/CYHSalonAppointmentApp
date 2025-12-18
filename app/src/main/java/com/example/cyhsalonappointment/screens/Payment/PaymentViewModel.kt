package com.example.cyhsalonappointment.screens.Payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyhsalonappointment.local.DAO.PaymentDao
import com.example.cyhsalonappointment.local.DAO.AppointmentDao
import com.example.cyhsalonappointment.local.entity.Appointment
import com.example.cyhsalonappointment.local.entity.Payment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val paymentDao: PaymentDao,
    private val appointmentDao: AppointmentDao
) : ViewModel() {

    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val payments: StateFlow<List<Payment>> = _payments.asStateFlow()

    // StateFlow for payments with service names
    private val _paymentsWithServices = MutableStateFlow<List<PaymentWithService>>(emptyList())
    val paymentsWithServices: StateFlow<List<PaymentWithService>> = _paymentsWithServices.asStateFlow()

    //  Simple data class to hold payment with service name
    data class PaymentWithService(
        val payment: Payment,
     val appointment: Appointment
    )

    fun loadPayments(customerId: String) {
        viewModelScope.launch {
            _payments.value = paymentDao.getPaymentsForCustomer(customerId)
        }
    }

    //   function to load payments WITH service names
    fun loadPaymentsWithServiceNames() {
        viewModelScope.launch {
            paymentDao.getAllPayments().collect { paymentList ->
                val result = mutableListOf<PaymentWithService>()

                for (payment in paymentList) {
                    val appointment = appointmentDao.getAppointmentById(payment.appointmentId)
                    if (appointment != null) {
                        result.add(
                            PaymentWithService(
                                payment = payment,
                                appointment = appointment
                            )
                        )
                    }
                }

                _paymentsWithServices.value = result
            }
        }
    }

    suspend fun getAppointment(appointmentId: String): Appointment? {
        return appointmentDao.getAppointmentById(appointmentId)
    }

    suspend fun getPaymentCount(): Int {
        return paymentDao.getPaymentCount()
    }

    fun createPayment(
        paymentId: String,
        appointmentId: String,
        purchaseAmount: Double,
        bookingFee: Double,
        taxAmount: Double,
        totalAmount: Double,
        paymentMethod: String,
        paymentDate: String,
        paymentTime: String
    ) {
        viewModelScope.launch {
            val payment = Payment(
                paymentId = paymentId,
                appointmentId = appointmentId,
                purchaseAmount = purchaseAmount,
                bookingFee = bookingFee,
                taxAmount = taxAmount,
                totalAmount = totalAmount,
                paymentMethod = paymentMethod,
                paymentDate = paymentDate,
                paymentTime = paymentTime,
                status = "Successful"
            )

            paymentDao.insertPayment(payment)
        }
    }
}