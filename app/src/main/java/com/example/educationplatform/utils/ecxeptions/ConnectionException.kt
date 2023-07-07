package com.example.educationplatform.utils.ecxeptions

class ConnectionException: java.lang.Exception() {
    override val message: String
        get() = "Network connection error"

    override fun equals(other: Any?): Boolean {
        return other is ConnectionException
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}