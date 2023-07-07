package com.example.educationplatform.presentation.cource.module.blockDiagram

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.BlockDiagramEditFragmentBinding
import com.example.educationplatform.presentation.TextEditorDialogFragment
import com.example.educationtools.base.EditableBlockBase

const val DIALOG_TAG = "dialog"

class BlockDiagramEditFragment: Fragment(R.layout.block_diagram_edit_fragment) {

    private val binding by viewBinding<BlockDiagramEditFragmentBinding>()
    private val args by navArgs<BlockDiagramEditFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.blockDiagramLayout.apply {
            blockDiagramEditor.loadConfigurations(args.diagramData)
            menuBlockEditor.adapter = blockDiagramEditor.generateMenuAdapter()
            menuBlockEditor.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            blockDiagramEditor.addOnBlockDoubleTouchListener { block ->
                showDialog(block)
            }
        }
        binding.infoButton.setOnClickListener {
            val action = BlockDiagramEditFragmentDirections.actionBlockDiagramEditFragmentToBlockDiagramTrainDialog()
            requireActivity().findNavController(R.id.container).navigate(action)
        }
        binding.applyButton.setOnClickListener {
            saveAndBack()
        }
        binding.backButton.setOnClickListener {
            requireActivity().findNavController(R.id.container).popBackStack()
        }
    }

    private fun saveAndBack() {
        val navController = requireActivity().findNavController(R.id.container)
        navController.previousBackStackEntry?.savedStateHandle?.set(args.parentFragmentKey, binding.blockDiagramLayout.blockDiagramEditor.saveConfigurations())
        navController.navigateUp()
    }

    private fun showDialog(block: EditableBlockBase) {
        val newFragment: TextEditorDialogFragment = TextEditorDialogFragment.newInstance(block)
        newFragment.show(childFragmentManager, DIALOG_TAG)
    }
}