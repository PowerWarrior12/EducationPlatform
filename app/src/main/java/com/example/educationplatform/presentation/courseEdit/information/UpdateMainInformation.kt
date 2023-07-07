package com.example.educationplatform.presentation.courseEdit.information

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.CourseCreateFragmentBinding
import com.example.educationplatform.presentation.courseEdit.CourseEditViewModel
import com.example.educationplatform.utils.extensions.launchWhenStarted
import com.example.educationplatform.utils.extensions.setWithProgressBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.onEach

private const val COURSE_UPDATING_MESSAGE = "UPDATING COURSE"
private const val ERROR_MESSAGE = "UPDATING ERROR"

class UpdateMainInformation: Fragment(R.layout.course_create_fragment) {
    private val binding by viewBinding<CourseCreateFragmentBinding>()
    private val viewModel by navGraphViewModels<CourseEditViewModel>(R.id.edit_course_graph)

    private val loadingSnack by lazy {
        Snackbar.make(binding.root, COURSE_UPDATING_MESSAGE, Snackbar.LENGTH_INDEFINITE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeModel()
    }

    private fun initViews() {
        binding.apply {
            toolbar.registrationLabel.text = resources.getString(R.string.update_course_toolbar)
            createButton.text = resources.getString(R.string.update_button)
            toolbar.actionButton.setOnClickListener {
                requireActivity().findNavController(R.id.container).popBackStack()
            }
            directionText.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    viewModel.updateDirection((p1 as TextView).text.toString())
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
            titleText.addTextChangedListener (object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel.updateTitle(p0.toString())
                }
                override fun afterTextChanged(p0: Editable?) {}
            })
            infoText.addTextChangedListener (object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel.updateInfo(p0.toString())
                }
                override fun afterTextChanged(p0: Editable?) {}
            })
            createButton.setOnClickListener {
                viewModel.updateCourse()
            }
            loadingSnack.setWithProgressBar()
        }
    }

    private fun observeModel() {
        viewModel.directionsFlow
            .onEach { directions ->
                val adapter = ArrayAdapter(requireContext(), R.layout.list_item, directions)
                binding.directionText.adapter = adapter
            }.launchWhenStarted(lifecycleScope)
        viewModel.errorFlow
            .onEach { error ->
                if (error)
                    Toast.makeText(requireContext(), ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
            }.launchWhenStarted(lifecycleScope)
        viewModel.courseFlow
            .onEach { course ->
                binding.apply {
                    titleText.setText(course.title)
                    infoText.setText(course.info)
                    viewModel.directionsFlow.collect { directions ->
                        directionText.setSelection(directions.indexOf(course.direction))
                    }
                }
            }.launchWhenStarted(lifecycleScope)
        viewModel.loadFlow
            .dropWhile { !it }
            .onEach { process ->
                when {
                    process -> loadingSnack.show()
                    else -> loadingSnack.dismiss()
                }
                setControllersEnable(!process)
            }.launchWhenStarted(lifecycleScope)
        viewModel.courseUpdatedFlow
            .onEach {
                requireActivity().findNavController(R.id.container).popBackStack()
            }.launchWhenStarted(lifecycleScope)
    }

    private fun setControllersEnable(enable: Boolean) {
        binding.apply {
            root.isEnabled = enable
            titleText.isEnabled = enable
            infoText.isEnabled = enable
            directionText.isEnabled = enable
        }
    }
}