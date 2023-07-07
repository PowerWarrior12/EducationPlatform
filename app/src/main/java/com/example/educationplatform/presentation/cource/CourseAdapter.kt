package com.example.educationplatform.presentation.cource

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.educationplatform.presentation.cource.information.CourseInformationFragment
import com.example.educationplatform.presentation.cource.modules.CourseModulesFragment
import com.example.educationplatform.presentation.cource.reports.CourseReportsFragment

class CourseAdapter(parentFragment: Fragment): FragmentStateAdapter(parentFragment) {

    private val fragments = arrayOf(
        CourseInformationFragment.Companion::newInstance,
        CourseReportsFragment.Companion::newInstance,
        CourseModulesFragment.Companion::newInstance,
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]()
    }
}