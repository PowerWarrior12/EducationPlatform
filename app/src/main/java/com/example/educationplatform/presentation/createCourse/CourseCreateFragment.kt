package com.example.educationplatform.presentation.createCourse

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.R
import com.example.educationplatform.databinding.CourseCreateFragmentBinding
import com.example.educationplatform.utils.extensions.launchWhenStarted
import com.example.educationplatform.utils.extensions.setWithProgressBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val COURSE_CREATING_MESSAGE = "CREATING COURSE"
private const val ERROR_MESSAGE = "CREATING ERROR"
class CourseCreateFragment: Fragment(R.layout.course_create_fragment) {
    @Inject
    lateinit var viewModelFactory: CourseCreateViewModel.CourseCreateViewModelFactory

    private val binding by viewBinding<CourseCreateFragmentBinding>()
    private val viewModel by viewModels<CourseCreateViewModel> {
        viewModelFactory
    }

    private val loadingSnack by lazy {
        Snackbar.make(binding.root, COURSE_CREATING_MESSAGE, Snackbar.LENGTH_INDEFINITE)
    }

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
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
        viewModel.processFlow
            .dropWhile { !it }
            .onEach { process ->
                when (process) {
                    true -> loadingSnack.show()
                    false -> loadingSnack.dismiss()
                }
                setControllersEnable(!process)
            }.launchWhenStarted(lifecycleScope)
        viewModel.courseCreatedFlow
            .dropWhile { it == null }
            .onEach { courseId ->
                openCourseScreen(courseId!!)
            }.launchWhenStarted(lifecycleScope)
    }

    private fun openCourseScreen(courseId: Int) {
        val action = CourseCreateFragmentDirections.actionCourseCreateFragmentToCourseEditFragment(courseId)
        requireActivity().findNavController(R.id.container).navigate(action)
    }

    private fun initViews() {
        binding.apply {
            toolbar.registrationLabel.text = resources.getString(R.string.create_course_toolbar)
            toolbar.actionButton.setOnClickListener {
                requireActivity().findNavController(R.id.container).popBackStack()
            }
            directionText.onItemSelectedListener = object: OnItemSelectedListener {
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
                viewModel.createCourse()
            }
            loadingSnack.setWithProgressBar()
        }
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