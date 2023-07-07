package com.example.educationplatform.domain.entities

data class Course(
    val id: Int = -1,
    val title: String,
    val info: String,
    val direction: String,
    val creatorId: Int,
    val rating: Int = 0,
    val usersCount: Int = 0,
    val imageUrl: String = ""
)