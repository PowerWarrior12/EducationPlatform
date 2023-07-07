package com.example.educationplatform.presentation.home.horizontalAdapter

import com.example.educationplatform.domain.entities.UsersCourse
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class EditCoursesAdapter(onEditCourseClick: (courseId: Int) -> Unit): AsyncListDifferDelegationAdapter<UsersCourse>(
    EditCourseDiffCallback,
    editCoursesAdapterDelegate(onEditCourseClick)
) {
}