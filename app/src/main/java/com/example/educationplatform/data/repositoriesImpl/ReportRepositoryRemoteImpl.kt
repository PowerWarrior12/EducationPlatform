package com.example.educationplatform.data.repositoriesImpl

import com.example.educationplatform.data.mappers.toCreateReportRequest
import com.example.educationplatform.data.mappers.toReport
import com.example.educationplatform.data.remote.report.ReportApi
import com.example.educationplatform.domain.entities.Report
import com.example.educationplatform.domain.repositories.ReportRepositoryRemote
import javax.inject.Inject

class ReportRepositoryRemoteImpl @Inject constructor(
    private val reportApi: ReportApi,
): ReportRepositoryRemote {
    override suspend fun getReportsByCourseId(courseId: Int): Result<List<Report>> {
        val response = reportApi.getCourseReports(courseId)
        return if (response.isSuccessful) {
            Result.success(response.body()!!.map { reportResponse ->
                reportResponse.toReport(courseId)
            })
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun createReport(report: Report): Result<Unit> {
        val response = reportApi.createReport(report.toCreateReportRequest(), report.courseId)
        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

}