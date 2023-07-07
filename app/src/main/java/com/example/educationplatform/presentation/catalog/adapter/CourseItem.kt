package com.example.educationplatform.presentation.catalog.adapter

import com.example.educationplatform.domain.entities.Course

sealed class CourseItem {
    class SimpleCourseItem(val course: Course) : CourseItem()
    object LoadingItem: CourseItem()
}
