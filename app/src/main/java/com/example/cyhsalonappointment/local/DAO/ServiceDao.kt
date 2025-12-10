package com.example.cyhsalonappointment.local.DAO

import androidx.room.*
import com.example.cyhsalonappointment.local.entity.SalonService
import com.example.cyhsalonappointment.local.entity.ServiceCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {

    // ---------------- CATEGORY CRUD ----------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: ServiceCategory)

    @Update
    suspend fun updateCategory(category: ServiceCategory)

    @Delete
    suspend fun deleteCategory(category: ServiceCategory)

    @Query("SELECT * FROM service_categories ORDER BY categoryName ASC")
    fun getCategories(): Flow<List<ServiceCategory>>

    @Query("SELECT * FROM service_categories WHERE categoryName = :name LIMIT 1")
    suspend fun getCategoryByName(name: String): ServiceCategory?

    // ---------------- SERVICE CRUD ----------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: SalonService)

    @Update
    suspend fun updateService(service: SalonService)

    // Hard delete (not used in UI, but kept if ever needed)
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
}
