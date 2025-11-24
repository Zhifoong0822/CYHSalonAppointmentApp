package com.example.cyhsalonappointment.screens.Customer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CustomerDAO {

    // Register a new customer
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCustomer(customer: CustomerEntity)

    // Login (match email + password)
    @Query("SELECT * FROM customers WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): CustomerEntity?

    // Get customer by ID
    @Query("SELECT * FROM customers WHERE customerId = :id")
    suspend fun getCustomerById(id: String): CustomerEntity?

    // Get all customers
    @Query("SELECT * FROM customers")
    suspend fun getAllCustomers(): List<CustomerEntity>
}