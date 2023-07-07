package com.example.educationplatform.presentation.cource.information

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.CourseInformationFragmentBinding
import com.example.educationplatform.presentation.cource.CourseViewModel
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CourseInformationFragment: Fragment(R.layout.course_information_fragment) {
    private val binding by viewBinding<CourseInformationFragmentBinding>()
    private val courseViewModel by navGraphViewModels<CourseViewModel>(R.id.course_graph)
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        job = lifecycleScope.launch {
            observeModel()
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }

    private fun observeModel() {
        courseViewModel.courseFlow
            .onEach { course ->
                binding.messageText.text = course.info
            }.launchWhenStarted(lifecycleScope)
    }

    companion object {
        fun newInstance(): CourseInformationFragment {
            return CourseInformationFragment()
        }
    }
}