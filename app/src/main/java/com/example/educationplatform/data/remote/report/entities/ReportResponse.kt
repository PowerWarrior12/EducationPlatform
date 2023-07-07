package com.example.educationplatform.data.remote.report.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReportResponse(

    @Json(name = "text")
    val text: String,
    @Json(name = "rating")
    val rating: Int,
    @Json(name = "createdAt")
    val date: String,
    @Json(name = "name")
    val userName: String,
    //@Json(name = "userIcon")
    //val userIcon: String
)