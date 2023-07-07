package com.example.educationplatform.domain.repositories

interface DeviceRepository {
    fun checkInternetConnection(): Boolean
}