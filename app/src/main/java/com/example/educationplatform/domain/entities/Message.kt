package com.example.educationplatform.domain.entities

data class Message(
    val userIcon: String,
    val userName: String,
    val text: String
) {
    companion object {
        fun empty(): Message {
            return Message(
                "", "", ""
            )
        }
    }
}