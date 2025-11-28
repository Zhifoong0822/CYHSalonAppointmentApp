package com.example.cyhsalonappointment.screens.Admin

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AdminDAO {

    // Login only (validate staffId + password)
    @Query("SELECT * FROM admin WHERE adminId = :id AND password = :password")
    suspend fun login(adminId: String, password: String): AdminEntity?
}