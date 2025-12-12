package com.example.cyhsalonappointment.data

import com.example.cyhsalonappointment.local.DAO.ServiceDao
import com.example.cyhsalonappointment.local.entity.SalonService
import kotlinx.coroutines.flow.Flow

class ServiceRepository(
    private val dao: ServiceDao
) {

    // ---------- SERVICES ONLY ----------
    fun getAllServices(): Flow<List<SalonService>> =
        dao.getAllServices()

    fun getServicesByCategory(categoryId: Int): Flow<List<SalonService>> =
        dao.getServicesByCategory(categoryId)

    suspend fun addService(service: SalonService) =
        dao.insertService(service)

    suspend fun updateService(service: SalonService) =
        dao.updateService(service)

    suspend fun deleteService(service: SalonService) =
        dao.deleteService(service)

    suspend fun softDelete(serviceId: Int) =
        dao.softDelete(serviceId)

    fun getServiceById(id: Int): Flow<SalonService?> =
        dao.getServiceById(id)

    fun getServiceByName(name: String): Flow<SalonService?> =
        dao.getServiceByName(name)
}
