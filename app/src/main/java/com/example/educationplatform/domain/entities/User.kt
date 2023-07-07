package com.example.educationplatform.domain.entities

data class User(
    val name: String,
    val secondName: String,
    val email: String,
    val info: String = "",
    val icon: String = "",
    val status: String = "",
    val password: String = ""
) {
    companion object {
        fun default() = User(
            "Peter",
            "Uhlimov",
            "businessmail1710@mail.ru",
            password = "1234567"
        )
    }
}