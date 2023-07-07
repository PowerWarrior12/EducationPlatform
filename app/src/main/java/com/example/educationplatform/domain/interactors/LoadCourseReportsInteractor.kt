package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.Report
import com.example.educationplatform.domain.repositories.ReportRepositoryRemote
import javax.inject.Inject

class LoadCourseReportsInteractor @Inject constructor(
    private val reportRepositoryRemote: ReportRepositoryRemote
) {
    suspend operator fun invoke(courseId: Int): Result<List<Report>> {
        return try {
            reportRepositoryRemote.getReportsByCourseId(courseId)
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }
}