package com.example.educationplatform.presentation.cource.reports.adapter

import androidx.recyclerview.widget.DiffUtil

object ReportItemDiffCallback: DiffUtil.ItemCallback<ReportItem>() {
    override fun areItemsTheSame(oldItem: ReportItem, newItem: ReportItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ReportItem, newItem: ReportItem): Boolean {
        return oldItem == newItem
    }
}