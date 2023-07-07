package com.example.educationplatform.presentation.courseEdit.structure.blockStageCreate

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.educationplatform.presentation.courseEdit.structure.blockDiagram.BlockDiagramFragment
import com.example.educationplatform.presentation.courseEdit.structure.task.TaskFragment
import com.example.educationplatform.presentation.courseEdit.structure.testCases.TestCasesFragment
import com.example.educationplatform.presentation.entities.BlockStageData

class BlockDataAdapter(parentFragment: Fragment): FragmentStateAdapter(parentFragment) {

    private val taskFragment = TaskFragment.newInstance()
    private val blockDiagramFragment = BlockDiagramFragment.newInstance()
    private val testCasesFragment = TestCasesFragment.newInstance()

    private var blockStageData: BlockStageData? = null

    private val fragments = arrayOf(
        taskFragment,
        blockDiagramFragment,
        testCasesFragment
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun setData(data: BlockStageData) {
        blockStageData = data
        taskFragment.setTaskText(data.task)
        blockDiagramFragment.setData(data.diagramData)
        testCasesFragment.setTestData(data.testData)
    }

    fun getData(): BlockStageData {
        val task = taskFragment.getText()?: blockStageData?.task ?: ""
        val diagramData = blockDiagramFragment.getData()?: blockStageData?.diagramData ?: ""
        val testData = testCasesFragment.getTestData()?: blockStageData?.testData ?: listOf()
        return BlockStageData(testData, diagramData, task)
    }
}