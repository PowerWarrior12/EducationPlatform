package com.example.educationplatform.presentation.courseEdit.structure.task

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.TaskFragmentBinding
import com.example.educationplatform.presentation.courseEdit.structure.TextEditorMenu
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.flow.onEach

class TaskFragment: Fragment(R.layout.task_fragment) {
    private val binding by viewBinding<TaskFragmentBinding>()
    private val viewModel by viewModels<TaskViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeModel()
    }

    fun setTaskText(text: String) {
        lifecycleScope.launchWhenStarted {
            viewModel.setData(text)
        }
    }

    fun getText(): String? {
        return if (isAdded)
            binding.textEditor.html ?: ""
        else null
    }

    private fun initViews() {
        binding.apply {
            TextEditorMenu(binding.menuTextEditor, binding.textEditor)
        }
    }

    private fun observeModel() {
        viewModel.dataFlow
            .onEach { binding.textEditor.html = it }
            .launchWhenStarted(lifecycleScope)
    }

    companion object {
        fun newInstance(): TaskFragment {
            return TaskFragment()
        }
    }
}