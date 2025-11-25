package com.example.cyhsalonappointment.screens.Customer

class CustomerRepository(private val customerDao: CustomerDAO) {

    // Register a new customer
    suspend fun registerCustomer(customer: CustomerEntity) {
        customerDao.insertCustomer(customer)
    }

    // Login: validate email + password
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
}