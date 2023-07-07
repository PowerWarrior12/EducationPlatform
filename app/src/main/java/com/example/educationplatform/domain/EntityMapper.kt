package com.example.educationplatform.domain

interface EntityMapper<F, T> {
    fun mapEntity(entityFrom: F): T
}