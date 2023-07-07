package com.example.educationplatform.presentation.catalog.adapter

import androidx.recyclerview.widget.DiffUtil

object CourseDiffCallback : DiffUtil.ItemCallback<CourseItem>() {
    override fun areItemsTheSame(oldItem: CourseItem, newItem: CourseItem): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: CourseItem, newItem: CourseItem): Boolean =
        oldItem == newItem
}