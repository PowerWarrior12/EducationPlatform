package com.example.educationplatform.domain.entities

data class CheckCodeData(
    val code: String,
    val language: String,
    //first is input, second is output
    val testDataList: List<Pair<String, String>>
)
