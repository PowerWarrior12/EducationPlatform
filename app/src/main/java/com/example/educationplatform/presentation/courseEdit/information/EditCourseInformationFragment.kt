package com.example.educationplatform.presentation.courseEdit.information

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.educationplatform.R
import com.example.educationplatform.databinding.EditCourseInfoFragmentBinding
import com.example.educationplatform.presentation.courseEdit.CourseEditFragmentDirections
import com.example.educationplatform.presentation.courseEdit.CourseEditViewModel
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.flow.onEach

class EditCourseInformationFragment: Fragment(R.layout.edit_course_info_fragment) {

    private val binding by viewBinding<EditCourseInfoFragmentBinding>()
    private val viewModel by navGraphViewModels<CourseEditViewModel>(R.id.edit_course_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeModel()
    }

    private fun initViews() {
        binding.apply {
            editButton.setOnClickListener {
                val action = CourseEditFragmentDirections.actionCourseEditFragmentToUpdateMainInformation()
                requireActivity().findNavController(R.id.container).navigate(action)
            }
            deleteButton.setOnClickListener {
                viewModel.deleteCourse()
            }
        }
    }

    private fun observeModel() {
        viewModel.courseFlow
            .onEach {  course ->
                binding.infoText.text = course.info

                Glide
                    .with(binding.root)
                    .load(course.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.teacher)
                    .into(binding.icon)
            }.launchWhenStarted(lifecycleScope)

        viewModel.onDeleteFlow
            .onEach { courseId ->
                requireActivity().findNavController(R.id.container).popBackStack()
            }.launchWhenStarted(lifecycleScope)
    }

    companion object {
        fun newInstance(): EditCourseInformationFragment {
            return EditCourseInformationFragment()
        }
    }
}