package com.example.educationplatform.domain.entities

data class TakesStage(
    val id: Int,
    val title: String,
    val info: String,
    val data: String = "",
    val type: String = "",
    val moduleId: Int,
    val totalScore: Int,
    val userScore: Int,
    val isDone: Boolean = false,
    val answer: String = ""
)