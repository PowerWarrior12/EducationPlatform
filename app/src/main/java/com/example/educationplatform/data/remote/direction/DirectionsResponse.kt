package com.example.educationplatform.data.remote.direction

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DirectionsResponse(
    @Json(name = "directions")
    val directions: List<String>
)
