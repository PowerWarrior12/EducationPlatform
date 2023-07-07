package com.example.educationplatform.presentation.courseEdit.structure.blockStageCreate

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.R
import com.example.educationplatform.databinding.BlockStageCreateFragmentBinding
import com.example.educationplatform.domain.entities.Stage
import com.example.educationplatform.presentation.courseEdit.CourseEditViewModel
import com.example.educationplatform.utils.TextWatcherImpl
import com.example.educationplatform.utils.extensions.launchWhenStarted
import com.example.educationplatform.utils.extensions.setWithProgressBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val PROCESS_MESSAGE = "PROCESS"
private const val ERROR_MESSAGE = "ERROR"

class BlockStageCreateFragment: Fragment(R.layout.block_stage_create_fragment) {
    @Inject
    lateinit var blockStageViewModelFactory: BlockStageCreateViewModel.BlockStageCreateViewModelFactory.Factory

    private val args: BlockStageCreateFragmentArgs by navArgs()
    private val binding by viewBinding<BlockStageCreateFragmentBinding>()
    private val courseViewModel by navGraphViewModels<CourseEditViewModel>(R.id.edit_course_graph)
    private val blockDataAdapter by lazy {
        BlockDataAdapter(this)
    }
    private val blockStageViewModel by viewModels<BlockStageCreateViewModel> {
        blockStageViewModelFactory.create(args.moduleId, args.stageId)
    }
    private val loadingSnack by lazy {
        Snackbar.make(binding.root, PROCESS_MESSAGE, Snackbar.LENGTH_INDEFINITE)
    }
    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeModel()
    }

    private fun initModel() {
        if (args.stageId >= 0) {
            val stage = courseViewModel.getStageById(args.stageId)
            binding.apply {
                editTitleText.setText(stage.title)
                editInfoText.setText(stage.info)
                editScoreText.setText(stage.score.toString())
                blockStageViewModel.setData(stage.data)
            }
        }
    }

    private fun initViews() {
        binding.apply {
            viewPager.isUserInputEnabled = false
            viewPager.adapter = blockDataAdapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when(position) {
                    0 -> tab.text = resources.getText(R.string.task_label)
                    1 -> tab.text = resources.getText(R.string.block_diagram_label)
                    else -> tab.text = resources.getText(R.string.test_data_label)
                }
            }.attach()

            editTitleText.addTextChangedListener(object : TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    blockStageViewModel.updateTitle(p0.toString())
                }
            })
            editInfoText.addTextChangedListener(object : TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    blockStageViewModel.updateInfo(p0.toString())
                }
            })

            editScoreText.addTextChangedListener(object : TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    blockStageViewModel.updateScore(p0.toString())
                }
            })

            saveButton.setOnClickListener {
                closeKeyBoard()
                save()
            }
            loadingSnack.setWithProgressBar()
        }
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
        }
    }

    private fun observeModel() {
        blockStageViewModel.dataFlow
            .onEach { data ->
                blockDataAdapter.setData(data)
            }.launchWhenStarted(lifecycleScope)

        blockStageViewModel.newStageFlow
            .onEach(::createNewStage)
            .launchWhenStarted(lifecycleScope)

        blockStageViewModel.updateStageFlow
            .onEach(::updateStage)
            .launchWhenStarted(lifecycleScope)

        blockStageViewModel.errorFlow
            .onEach { error ->
                if (error) {
                    Toast.makeText(requireContext(), ERROR_MESSAGE, Toast.LENGTH_LONG).show()
                }
            }.launchWhenStarted(lifecycleScope)

        blockStageViewModel.processFlow
            .onEach { process ->
                when {
                    process -> loadingSnack.show()
                    else -> loadingSnack.dismiss()
                }
                setEnabledControllers(!process)
            }.launchWhenStarted(lifecycleScope)
    }


    private fun createNewStage(stage: Stage) {
        courseViewModel.addModuleStageItem(stage)
        requireActivity().findNavController(R.id.container).popBackStack()
    }

    private fun updateStage(stage: Stage) {
        courseViewModel.updateModuleStageItems(stage)
        requireActivity().findNavController(R.id.container).popBackStack()
    }

    private fun save() {
        blockStageViewModel.saveData(blockDataAdapter.getData())
    }
}