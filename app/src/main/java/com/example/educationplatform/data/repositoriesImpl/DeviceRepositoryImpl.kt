package com.example.educationplatform.data.repositoriesImpl

import com.example.educationplatform.data.local.InternetManager
import com.example.educationplatform.domain.repositories.DeviceRepository
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val internetManager: InternetManager
): DeviceRepository {
    override fun checkInternetConnection(): Boolean {
        return internetManager.checkInternetConnection()
    }
}