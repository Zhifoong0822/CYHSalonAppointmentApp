package com.example.cyhsalonappointment.local.DAO

import androidx.room.*
import com.example.cyhsalonappointment.local.entity.SalonService
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {

    // ---------------- SERVICE CRUD ----------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: SalonService)

    @Update
    suspend fun updateService(service: SalonService)

    // Hard delete
    @Delete
    suspend fun deleteService(service: SalonService)

    // Soft delete -> hide from UI and booking
    @Query("UPDATE services SET isActive = 0 WHERE serviceId = :serviceId")
    suspend fun softDelete(serviceId: Int)

    // Only ACTIVE services are returned
    @Query("SELECT * FROM services WHERE isActive = 1 ORDER BY serviceName ASC")
    fun getAllServices(): Flow<List<SalonService>>

    @Query(
        "SELECT * FROM services " +
                "WHERE categoryId = :categoryId AND isActive = 1 " +
                "ORDER BY serviceName ASC"
    )
    fun getServicesByCategory(categoryId: Int): Flow<List<SalonService>>

    @Query("SELECT * FROM services WHERE serviceId = :id LIMIT 1")
    fun getServiceById(id: Int): Flow<SalonService?>

    @Query("SELECT * FROM services WHERE serviceName = :name LIMIT 1")
    fun getServiceByName(name: String): Flow<SalonService?>

    @Query("SELECT serviceId FROM services WHERE serviceName = :name LIMIT 1")
    suspend fun getServiceIdByName(name: String): Int?


    @Query("""
    SELECT COUNT(*) 
    FROM services 
    WHERE serviceName = :name 
      AND categoryId = :categoryId 
      AND isActive = 1
""")
    suspend fun serviceExists(
        name: String,
        categoryId: Int
    ): Int

}
