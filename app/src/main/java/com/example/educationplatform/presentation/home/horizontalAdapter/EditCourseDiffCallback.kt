package com.example.educationplatform.presentation.home.horizontalAdapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.example.educationplatform.domain.entities.UsersCourse

object EditCourseDiffCallback : DiffUtil.ItemCallback<UsersCourse>() {
    override fun areItemsTheSame(oldItem: UsersCourse, newItem: UsersCourse): Boolean =
        oldItem == newItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: UsersCourse, newItem: UsersCourse): Boolean =
        oldItem == newItem
}