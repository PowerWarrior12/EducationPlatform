package com.example.educationplatform.utils

fun emailValidation(email: String): Boolean {
    return emailRegex.matches(email)
}

fun passwordValidation(password: String): Boolean {
    return passwordRegex.matches(password)
}

fun nameValidation(name: String): Boolean {
    return nameRegex.matches(name)
}