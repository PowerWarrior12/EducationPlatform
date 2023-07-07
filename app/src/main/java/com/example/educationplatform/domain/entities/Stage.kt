package com.example.educationplatform.domain.entities

data class Stage(
    val id: Int,
    val title: String,
    val info: String,
    val data: String = "",
    val type: String = "",
    val moduleId: Int,
    val score: Int
)