package com.example.educationplatform.domain.entities

data class Module(
    val id: Int,
    val title: String,
    val info: String,
    val courseId: Int,
    val score: Int,
    val stages: List<Stage>
) {
}