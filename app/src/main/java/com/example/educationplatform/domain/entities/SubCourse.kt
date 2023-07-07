package com.example.educationplatform.domain.entities

data class SubCourse(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val totalScore: Int,
    val userScore: Int
) {
    fun getScoreFormat(): String {
        return "$userScore/$totalScore"
    }
}