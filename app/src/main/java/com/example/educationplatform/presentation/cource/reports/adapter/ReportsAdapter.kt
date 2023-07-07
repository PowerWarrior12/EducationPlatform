package com.example.educationplatform.presentation.cource.reports.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class ReportsAdapter(
    onSendClick: (message: String, rating: Int) -> Unit
): AsyncListDifferDelegationAdapter<ReportItem>(
    ReportItemDiffCallback,
    sendReportAdapter(onSendClick),
    userReportAdapter()
)