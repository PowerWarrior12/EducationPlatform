package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.report.entities.CreateReportRequest
import com.example.educationplatform.domain.entities.Report

fun Report.toCreateReportRequest(): CreateReportRequest {
    return CreateReportRequest(
        text = text,
        rating = rating
    )
}