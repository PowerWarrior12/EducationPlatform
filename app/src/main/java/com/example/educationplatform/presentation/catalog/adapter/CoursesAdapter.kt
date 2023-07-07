package com.example.educationplatform.presentation.catalog.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class CoursesAdapter(
    onCourseClick: (courseId: Int) -> Unit
) : AsyncListDifferDelegationAdapter<CourseItem>(
    CourseDiffCallback,
    coursesAdapterDelegate(onCourseClick),
    loadingItemAdapterDelegate()
)