package com.example.educationplatform.data.remote.codeExecution.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CodeExecutionRequest(
    @Json(name = "clientId")
    val clientId: String,
    @Json(name = "clientSecret")
    val clientSecret: String,
    @Json(name = "script")
    val script: String,
    @Json(name = "stdin")
    val inputVars: String,
    @Json(name = "language")
    val language: String,
    @Json(name = "versionIndex")
    val versionIndex: String = "0",
    @Json(name = "compileOnly")
    val compileOnly: Boolean = false
)