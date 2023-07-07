package com.example.educationplatform.domain.repositories

import com.example.educationplatform.domain.entities.Report

interface ReportRepositoryRemote {
    suspend fun getReportsByCourseId(courseId: Int): Result<List<Report>>
    suspend fun createReport(report: Report): Result<Unit>
}