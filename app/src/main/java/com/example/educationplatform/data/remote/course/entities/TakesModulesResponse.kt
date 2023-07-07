package com.example.educationplatform.data.remote.course.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TakesModulesResponse(
    @Json(name = "modules")
    val modules: List<TakesModuleResponse>
)
