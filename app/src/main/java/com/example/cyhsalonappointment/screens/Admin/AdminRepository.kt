package com.example.cyhsalonappointment.screens.Admin

class AdminRepository(private val adminDao: AdminDAO) {

    suspend fun validateStaffLogin(adminId: String, password: String): Boolean {
        val admin = adminDao.login(adminId, password)
        return admin?.password == password
    }

    suspend fun getAdmin(adminId: String, password: String): AdminEntity? {
        return adminDao.login(adminId, password)
    }
}