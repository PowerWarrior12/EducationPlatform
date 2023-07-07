package com.example.educationplatform.presentation.courseEdit.structure

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.CourseStructureFragmentBinding
import com.example.educationplatform.presentation.courseEdit.CourseEditFragmentDirections
import com.example.educationplatform.presentation.courseEdit.CourseEditViewModel
import com.example.educationplatform.presentation.courseEdit.structure.adapter.StructureBlocksAdapter
import com.example.educationplatform.utils.decorators.AroundBorderDecoration
import com.example.educationplatform.utils.decorators.MarginDecorator
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.flow.onEach

class StructureCourseFragment: Fragment(R.layout.course_structure_fragment) {

    private val viewModel: CourseEditViewModel by navGraphViewModels(R.id.edit_course_graph)
    private val binding by viewBinding<CourseStructureFragmentBinding>()
    private val adapter = StructureBlocksAdapter(
        ::onModuleClick,
        ::onStageClick,
        ::onModuleDelete,
        ::onStageDelete,
        ::onModuleAdd,
        ::onStageAdd
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun observeModel() {
        viewModel.structureBlockItemsFlow
            .onEach { blocks ->
                adapter.items = blocks
            }.launchWhenStarted(lifecycleScope)
    }

    private fun initViews() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.addItemDecoration(MarginDecorator(listOf(1, 3), left = 164))
            recyclerView.addItemDecoration(AroundBorderDecoration())
            recyclerView.adapter = adapter
        }
    }

    private fun onModuleClick(moduleId: Int, courseId: Int) {
        val action = CourseEditFragmentDirections.actionCourseEditFragmentToModuleCreateDialogFragment(courseId).apply {
            setModuleId(moduleId)
        }
        requireActivity().findNavController(R.id.container).navigate(action)
    }

    private fun onStageClick(stageId: Int, moduleId: Int, stageType: String) {
        val action = when(stageType) {
            StageTypes.Lecture.name -> {
                CourseEditFragmentDirections.actionCourseEditFragmentToLectureStageCreateFragment(moduleId).apply {
                    setStageId(stageId)
                }
            }
            StageTypes.Code.name -> {
                CourseEditFragmentDirections.actionCourseEditFragmentToCodeStageCreateFragment(moduleId).apply {
                    setStageId(stageId)
                }
            }
            else -> {
                CourseEditFragmentDirections.actionCourseEditFragmentToBlockStageCreateFragment(moduleId).apply {
                    setStageId(stageId)
                }
            }
        }
        action.let {
            requireActivity().findNavController(R.id.container).navigate(action)
        }
    }

    private fun onModuleDelete(moduleId: Int) {
        viewModel.deleteModule(moduleId)
    }

    private fun onStageDelete(stageId: Int) {
        viewModel.deleteStage(stageId)
    }

    private fun onModuleAdd(courseId: Int) {
        val action = CourseEditFragmentDirections.actionCourseEditFragmentToModuleCreateDialogFragment(courseId)
        requireActivity().findNavController(R.id.container).navigate(action)
    }

    private fun onStageAdd(moduleId: Int) {
        val action = CourseEditFragmentDirections.actionCourseEditFragmentToSelectStageTypeDialog(moduleId)
        requireActivity().findNavController(R.id.container).navigate(action)
    }

    companion object {
        fun newInstance(): StructureCourseFragment {
            return StructureCourseFragment()
        }
    }
}