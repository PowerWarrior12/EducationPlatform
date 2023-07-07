package com.example.educationtools.examples

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationtools.R
import com.example.educationtools.base.EditableBlockBase
import com.example.educationtools.databinding.DialogFragmentBinding

class TextDialogFragment: DialogFragment(R.layout.dialog_fragment) {
    private val binding by viewBinding<DialogFragmentBinding>()
    private var text = ""
    private var block: EditableBlockBase? = null
        set(value)  {
            field = value
            text = value?.getText() ?: text
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE,
            android.R.style.ThemeOverlay_Material_Dialog_Alert);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.editText.setText(text)
        binding.editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        super.onViewCreated(view, savedInstanceState)
        binding.saveButton.setOnClickListener {
            block?.setText(text)
            dismiss()
        }
    }

    companion object {
        fun newInstance(editableBlockBase: EditableBlockBase): TextDialogFragment {
            return TextDialogFragment().apply {
                block = editableBlockBase
            }
        }
    }
}