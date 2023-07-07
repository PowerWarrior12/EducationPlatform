package com.example.educationplatform.data.mappers

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import com.example.educationplatform.data.remote.report.entities.ReportResponse
import com.example.educationplatform.domain.entities.Report

@SuppressLint("SimpleDateFormat")
fun ReportResponse.toReport(courseId: Int): Report {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    return Report(
        id = -1,
        courseId = courseId,
        userName = userName,
        userIcon = "",
        text = text,
        rating = rating,
        date = dateFormat.parse(date)
    )
}