package com.example.educationplatform.presentation.courseEdit

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.educationplatform.presentation.courseEdit.information.EditCourseInformationFragment
import com.example.educationplatform.presentation.courseEdit.structure.StructureCourseFragment

class CourseEditAdapter(parentFragment: Fragment): FragmentStateAdapter(parentFragment) {

    private val fragments = arrayOf(
        EditCourseInformationFragment.Companion::newInstance,
        StructureCourseFragment.Companion::newInstance
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]()
    }
}