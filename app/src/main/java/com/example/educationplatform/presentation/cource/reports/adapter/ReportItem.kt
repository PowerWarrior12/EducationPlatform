package com.example.educationplatform.presentation.cource.reports.adapter

import com.example.educationplatform.domain.entities.Report
import com.example.educationplatform.domain.entities.User

sealed class ReportItem {
    class UserReportItem(val report: Report): ReportItem()
    class AddReportItem(val user: User): ReportItem()
}
