package com.example.educationplatform.presentation.courseEdit.structure.codeStageCreate

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.R
import com.example.educationplatform.databinding.CodeStageCreateFragmentBinding
import com.example.educationplatform.domain.entities.Stage
import com.example.educationplatform.presentation.courseEdit.CourseEditViewModel
import com.example.educationplatform.presentation.courseEdit.structure.TextEditorMenu
import com.example.educationplatform.presentation.courseEdit.structure.testCasesAdapter.TestCasesAdapter
import com.example.educationplatform.presentation.courseEdit.structure.testCasesAdapter.TestDataCollector
import com.example.educationplatform.presentation.languagesMap
import com.example.educationplatform.utils.TextWatcherImpl
import com.example.educationplatform.utils.decorators.MarginDecorator
import com.example.educationplatform.utils.extensions.launchWhenStarted
import com.example.educationplatform.utils.extensions.setWithProgressBar
import com.google.android.material.snackbar.Snackbar
import de.markusressel.kodehighlighter.language.java.JavaRuleBook
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val PROCESS_MESSAGE = "PROCESS"
private const val ERROR_MESSAGE = "ERROR"

class CodeStageCreateFragment : Fragment(R.layout.code_stage_create_fragment) {
    @Inject
    lateinit var codeStageCreateFactory: CodeStageCreateViewModel.CodeStageCreateViewModelFactory.Factory

    private val binding by viewBinding<CodeStageCreateFragmentBinding>()
    private val args: CodeStageCreateFragmentArgs by navArgs()
    private val testDataCollector = TestDataCollector()
    private val courseViewModel by navGraphViewModels<CourseEditViewModel>(R.id.edit_course_graph)
    private val codeStageViewModel: CodeStageCreateViewModel by viewModels {
        codeStageCreateFactory.create(args.moduleId, args.stageId)
    }
    private val loadingSnack by lazy {
        Snackbar.make(binding.root, PROCESS_MESSAGE, Snackbar.LENGTH_INDEFINITE)
    }
    private val testDataAdapter by lazy {
        TestCasesAdapter(codeStageViewModel::deleteTestItem, testDataCollector) {
            addTestItem()
        }
    }
    private val languageAdapter by lazy {
        ArrayAdapter(requireContext(), R.layout.list_item, languagesMap.keys.toList())
    }

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initCodeCreateModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeModel()
    }

    private fun initViews() {
        binding.apply {
            TextEditorMenu(binding.textEditorMenu, binding.textEditor).init()
            languageSpinner.adapter = languageAdapter
            languageSpinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    codeEditor.languageRuleBook = (languagesMap[(p1 as TextView).text.toString()])
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
            testRecycler.apply {
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(
                    MarginDecorator(
                        listOf(0, 1),
                        left = 64,
                        right = 64,
                        top = 64,
                        bottom = 64
                    )
                )
                adapter = testDataAdapter
            }
            codeEditor.languageRuleBook = JavaRuleBook()
            codeEditor.showMinimap = false
            binding.saveButton.setOnClickListener {
                closeKeyBoard()
                save()
            }
            editTitleText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    codeStageViewModel.updateTitle(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
            editInfoText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    codeStageViewModel.updateInfo(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {}
            })

            editScoreText.addTextChangedListener(object : TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    codeStageViewModel.updateScore(p0.toString())
                }
            })
        }
        loadingSnack.setWithProgressBar()
    }

    private fun initCodeCreateModel() {
        if (args.stageId >= 0) {
            val stage = courseViewModel.getStageById(args.stageId)
            binding.apply {
                editTitleText.setText(stage.title)
                editInfoText.setText(stage.info)
                editScoreText.setText(stage.score.toString())
                codeStageViewModel.setData(stage.data)
            }
        }
    }

    private fun observeModel() {
        codeStageViewModel.codeFlow
            .onEach { code ->
                binding.codeEditor.text = code
            }
            .launchWhenStarted(lifecycleScope)

        codeStageViewModel.taskFlow
            .onEach { text ->
                binding.textEditor.html = text
            }
            .launchWhenStarted(lifecycleScope)

        codeStageViewModel.testFlow
            .onEach { testItems ->
                testDataAdapter.items = testItems
            }
            .launchWhenStarted(lifecycleScope)

        codeStageViewModel.languageFlow
            .onEach { language ->
                binding.languageSpinner.setSelection(languagesMap.keys.indexOf(language))
            }
            .launchWhenStarted(lifecycleScope)

        codeStageViewModel.newStageFlow
            .onEach { stage ->
                createNewStage(stage)
            }.launchWhenStarted(lifecycleScope)

        codeStageViewModel.updateStageFlow
            .onEach { stage ->
                updateStage(stage)
            }.launchWhenStarted(lifecycleScope)

        codeStageViewModel.errorFlow
            .onEach { error ->
                if (error) {
                    Toast.makeText(requireContext(), ERROR_MESSAGE, Toast.LENGTH_LONG).show()
                }
            }.launchWhenStarted(lifecycleScope)

        codeStageViewModel.processFlow
            .onEach { process ->
                when {
                    process -> loadingSnack.show()
                    else -> loadingSnack.dismiss()
                }
                setEnabledControllers(!process)
            }.launchWhenStarted(lifecycleScope)
    }

    private fun closeKeyBoard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun setEnabledControllers(enable: Boolean) {
        binding.apply {
            titleText.isEnabled = enable
            saveButton.isEnabled = enable
            infoText.isEnabled = enable
            languageSpinner.isEnabled = enable
            codeEditor.isEnabled = enable
        }
    }

    private fun createNewStage(stage: Stage) {
        courseViewModel.addModuleStageItem(stage)
        requireActivity().findNavController(R.id.container).popBackStack()
    }

    private fun updateStage(stage: Stage) {
        courseViewModel.updateModuleStageItems(stage)
        requireActivity().findNavController(R.id.container).popBackStack()
    }

    private fun addTestItem() {
        codeStageViewModel.addTestItem()
    }

    private fun save() {
        codeStageViewModel.saveCodeStageData(
            binding.codeEditor.text,
            binding.languageSpinner.selectedItem.toString(),
            testDataCollector.getData(),
            binding.textEditor.html
        )
    }
}