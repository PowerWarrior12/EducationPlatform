package com.example.educationplatform.presentation.courseEdit.structure

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.StageSelectorDialogFragmentBinding

class SelectStageTypeDialog: DialogFragment(R.layout.stage_selector_dialog_fragment) {
    private val binding by viewBinding<StageSelectorDialogFragmentBinding>()
    private val args: SelectStageTypeDialogArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE,
            android.R.style.ThemeOverlay_Material_Dialog_Alert)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            lectureButton.setOnClickListener {
                val action = SelectStageTypeDialogDirections.actionSelectStageTypeDialogToLectureStageCreateFragment(args.moduleId)
                requireActivity().findNavController(R.id.container).navigate(action)
            }
            blockDiagramButton.setOnClickListener {
                val action = SelectStageTypeDialogDirections.actionSelectStageTypeDialogToBlockStageCreateFragment(args.moduleId)
                requireActivity().findNavController(R.id.container).navigate(action)
            }
            codeButton.setOnClickListener {
                val action = SelectStageTypeDialogDirections.actionSelectStageTypeDialogToCodeStageCreateFragment(args.moduleId)
                requireActivity().findNavController(R.id.container).navigate(action)
            }
        }
    }
}