package com.example.educationplatform.presentation.home.mainAdapter

import android.os.Parcelable
import com.example.educationplatform.domain.entities.SubCourse
import com.example.educationplatform.domain.entities.UsersCourse

sealed class UsersCourseItem {
    data class TakesCourseItem(val subCourse: SubCourse): UsersCourseItem()
    data class HorizontalItems(val userCourses: List<UsersCourse>, var state: Parcelable? = null): UsersCourseItem()
    data class TitleItem(val title: String): UsersCourseItem()
    object ButtonItem: UsersCourseItem()
    object LoadingItem: UsersCourseItem()
}