package com.example.cyhsalonappointment.screens.Customer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CustomerDAO {

    // Register a new customer
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCustomer(customer: CustomerEntity)

    // Login (match email + password)
    @Query("SELECT * FROM customers WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): CustomerEntity?

    // Get customer by Email
    @Query("SELECT * FROM customers WHERE email = :email LIMIT 1")
    suspend fun getCustomerByEmail(email: String): CustomerEntity?

    // Get customer by ID
    @Query("SELECT * FROM customers WHERE customerId = :id LIMIT 1")
    suspend fun getCustomerById(id: String): CustomerEntity?

    @Query("SELECT * FROM customers WHERE username = :username LIMIT 1")
    suspend fun getCustomerByUsername(username: String): CustomerEntity?

    // Get all customers
    @Query("SELECT * FROM customers")
    suspend fun getAllCustomers(): List<CustomerEntity>

    @Query("SELECT customerId FROM customers")
    suspend fun getAllCustomerIds(): List<String>

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)

    @Query("""
    UPDATE customers 
    SET username = :username,
        gender = :gender,
        email = :email,
        contactNumber = :contactNumber,
        updatedAt = :updatedAt
    WHERE customerId = :customerId
""")
    suspend fun updateCustomerInfo(
        customerId: String,
        username: String,
        gender: String,
        email: String,
        contactNumber: String,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("DELETE FROM customers WHERE customerId = :id")
    suspend fun deleteCustomer(id: String)
}