package com.example.cyhsalonappointment.screens.Customer

import android.util.Log

class CustomerRepository(private val customerDao: CustomerDAO) {

    // SIGNUP
    suspend fun signUp(customer: CustomerEntity): AuthResult<CustomerEntity> {
        return try {
            customerDao.insertCustomer(customer)
            AuthResult.Success(customer)
        } catch (e: Exception) {
            AuthResult.Error(e)
        }
    }

    // LOGIN
    suspend fun loginCustomer(email: String, password: String): CustomerEntity? {
        return customerDao.login(email, password)
    }

    // Get all customers (optional)
    suspend fun getAllCustomers(): List<CustomerEntity> {
        return customerDao.getAllCustomers()
    }

    // Get customer by ID
    suspend fun getCustomerById(id: String): CustomerEntity? {
        return customerDao.getCustomerById(id)
    }

    suspend fun getAllCustomerIds(): List<String> {
        return customerDao.getAllCustomerIds()
    }

    suspend fun isUsernameAvailable(username: String): Boolean {
        return customerDao.getCustomerByUsername(username) == null
    }

    suspend fun loadCustomerProfile(email: String): CustomerEntity? {
        return customerDao.getCustomerByEmail(email)
    }

    suspend fun updateCustomerProfile(
        customerId: String,
        username: String,
        gender: String,
        email: String,
        contactNumber: String
    ) {
        customerDao.updateCustomerInfo(
            customerId = customerId,
            username = username,
            gender = gender,
            email = email,
            contactNumber = contactNumber
        )
    }

    suspend fun deleteCustomer(userId: String) {
        customerDao.deleteCustomer(userId)
    }
}