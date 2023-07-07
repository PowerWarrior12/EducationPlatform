package com.example.educationplatform.presentation.home.mainAdapter

import android.view.View
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class UsersCoursesAdapter(
    onTakesCourseClick: (courseId: Int) -> Unit,
    onEditCourseClick: (courseId: Int) -> Unit,
    onNewCourseClick: View.OnClickListener
) : AsyncListDifferDelegationAdapter<UsersCourseItem>(
    UserCourseDiffCallback,
    titleAdapterDelegate(),
    takesCoursesAdapterDelegate(onTakesCourseClick),
    horizontalItemAdapterDelegate(onEditCourseClick),
    buttonAdapterDelegate(onNewCourseClick),
    loadingItemAdapterDelegate()
)