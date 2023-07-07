package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.Report
import com.example.educationplatform.domain.repositories.ReportRepositoryRemote
import javax.inject.Inject

class SendReportInteractor @Inject constructor(private val reportRepositoryRemote: ReportRepositoryRemote) {
    suspend operator fun invoke(report: Report): Result<Unit> {
        return reportRepositoryRemote.createReport(report)
    }
}