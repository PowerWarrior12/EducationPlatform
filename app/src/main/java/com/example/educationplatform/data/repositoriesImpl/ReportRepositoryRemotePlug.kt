package com.example.educationplatform.data.repositoriesImpl

import com.example.educationplatform.domain.entities.Report
import com.example.educationplatform.domain.repositories.ReportRepositoryRemote
import javax.inject.Inject

class ReportRepositoryRemotePlug @Inject constructor(): ReportRepositoryRemote {

    override suspend fun getReportsByCourseId(courseId: Int): Result<List<Report>> {
        return Result.success(
            listOf(
            )
        )
    }

    override suspend fun createReport(report: Report): Result<Unit> {
        return Result.success(Unit)
    }
}