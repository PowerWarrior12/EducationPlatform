package com.example.educationplatform.domain.entities

data class CodeForExecution (
    val script: String,
    val inputVars: String,
    val language: String
)