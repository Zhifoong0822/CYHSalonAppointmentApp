package com.example.cyhsalonappointment.screens.Staff

import androidx.room.Dao
import androidx.room.Query

@Dao
interface StaffDAO {

    // Login only (validate staffId + password)
    @Query("SELECT * FROM staff WHERE staffId = :id AND password = :password")
    suspend fun login(id: String, password: String): StaffEntity?
}