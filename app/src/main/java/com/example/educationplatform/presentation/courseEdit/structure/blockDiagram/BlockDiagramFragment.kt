package com.example.educationplatform.presentation.courseEdit.structure.blockDiagram

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.BlockDiagramFragmentBinding
import com.example.educationplatform.presentation.TextEditorDialogFragment
import com.example.educationplatform.presentation.cource.module.blockDiagram.DIALOG_TAG
import com.example.educationplatform.utils.extensions.launchWhenStarted
import com.example.educationtools.base.EditableBlockBase
import kotlinx.coroutines.flow.onEach

class BlockDiagramFragment : Fragment(R.layout.block_diagram_fragment) {
    private val binding by viewBinding<BlockDiagramFragmentBinding>()
    private val viewModel by viewModels<BlockDiagramViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeModel()
    }

    fun setData(data: String) {
        lifecycleScope.launchWhenStarted {
            viewModel.setData(data)
        }
    }

    fun getData(): String? {
        return if (isAdded) binding.blockDiagramEditor.saveConfigurations()
        else null
    }

    private fun initViews() {
        binding.apply {
            menuBlockEditor.adapter = blockDiagramEditor.generateMenuAdapter()
            menuBlockEditor.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            blockDiagramEditor.addOnBlockDoubleTouchListener { block ->
                showDialog(block)
            }
        }
    }

    private fun observeModel() {
        viewModel.dataFlow.onEach { binding.blockDiagramEditor.loadConfigurations(it) }
            .launchWhenStarted(lifecycleScope)
    }

    private fun showDialog(block: EditableBlockBase) {
        val newFragment: TextEditorDialogFragment = TextEditorDialogFragment.newInstance(block)
        newFragment.show(childFragmentManager, DIALOG_TAG)
    }

    companion object {
        fun newInstance(): BlockDiagramFragment {
            return BlockDiagramFragment()
        }
    }
}