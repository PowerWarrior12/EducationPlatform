package com.example.educationplatform.presentation.home.mainAdapter

import androidx.recyclerview.widget.DiffUtil

object UserCourseDiffCallback : DiffUtil.ItemCallback<UsersCourseItem>() {
    override fun areItemsTheSame(oldItem: UsersCourseItem, newItem: UsersCourseItem): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: UsersCourseItem, newItem: UsersCourseItem): Boolean =
        oldItem == newItem
}