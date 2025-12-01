package com.example.cyhsalonappointment.screens.Admin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AdminDAO {

    // Login only (validate staffId + password)
    @Query("SELECT * FROM admin WHERE adminId = :adminId AND password = :password")
    suspend fun login(adminId: String, password: String): AdminEntity?

    @Query("SELECT * FROM admin")
    suspend fun getAllAdmins(): List<AdminEntity>

    @Insert
    suspend fun insertAdmin(admin: AdminEntity)
}