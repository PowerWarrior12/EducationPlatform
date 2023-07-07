package com.example.educationplatform.presentation

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.educationplatform.R

class BlockDiagramTrainDialog: DialogFragment(R.layout.train_block_diagram_layout) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            DialogFragment.STYLE_NO_TITLE,
            android.R.style.ThemeOverlay_Material_Dialog_Alert
        );
    }
}