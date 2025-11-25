package com.example.cyhsalonappointment.screens.Staff

class StaffRepository(private val staffDao: StaffDAO) {

    suspend fun validateStaffLogin(staffId: String, password: String): Boolean {
        val staff = staffDao.login(staffId, password)
        return staff?.password == password
    }

    suspend fun getStaff(staffId: String, password: String): StaffEntity? {
        return staffDao.login(staffId, password)
    }
}