package com.example.cyhsalonappointment.screens.Customer

import android.util.Log

class CustomerRepository(private val customerDao: CustomerDAO) {

    // SIGNUP
    suspend fun signUp(customer: CustomerEntity): AuthResult<CustomerEntity> {
        return try {
            Log.d("Repo", "Inserting customer: $customer")
            val existing = customerDao.getCustomerByEmail(customer.email)
            if (existing != null) return AuthResult.Error(Exception("Email already in use"))

            customerDao.insertCustomer(customer)
            Log.d("Repo", "Inserted successfully")
            AuthResult.Success(customer)
        } catch (e: Exception) {
            Log.e("Repo", "Signup failed", e)
            AuthResult.Error(Exception("Signup failed: ${e.message}"))
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

    suspend fun isUsernameAvailable(username: String): Boolean {
        return customerDao.getCustomerByUsername(username) == null
    }

    suspend fun loadCustomerProfile(email: String): CustomerEntity? {
        return customerDao.getCustomerByEmail(email)
    }

    suspend fun updateCustomer(customer: CustomerEntity): Boolean {
        return try {
            customerDao.updateCustomer(customer) // make sure your DAO has an @Update function
            true
        } catch (e: Exception) {
            false
        }
    }
    suspend fun deleteCustomer(userId: String) {
        customerDao.deleteCustomer(userId)
    }
}