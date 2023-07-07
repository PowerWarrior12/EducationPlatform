package com.example.educationplatform.domain.entities

data class TakesModule(
    val id: Int,
    val totalScore: Int,
    val title: String,
    val info: String,
    val courseId: Int,
    val userScore: Int,
    val stages: MutableList<TakesStage>
) {
    fun getFormatScore(): String {
        return "$userScore/$totalScore"
    }
}