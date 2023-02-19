package com.example.educationplatform.domain.entities

data class User(
    val id: Int,
    val name: String,
    val secondName: String,
    val password: String,
    val email: String,
    val info: String = ""
)