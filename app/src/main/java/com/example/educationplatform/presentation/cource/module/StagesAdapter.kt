package com.example.educationplatform.presentation.cource.module

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.educationplatform.domain.entities.TakesStage
import com.example.educationplatform.presentation.cource.module.blockDiagram.BlockDiagramDisplayFragment
import com.example.educationplatform.presentation.cource.module.coding.CodingDisplayFragment
import com.example.educationplatform.presentation.cource.module.lecture.LectureDisplayFragment
import com.example.educationplatform.presentation.courseEdit.structure.StageTypes

class StagesAdapter(parentFragment: Fragment) :
    FragmentStateAdapter(parentFragment) {

    private val stages: MutableList<TakesStage> = mutableListOf()
    private val fragments: MutableList<Fragment> = mutableListOf()

    override fun getItemCount(): Int {
        return stages.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateStages(newStages: List<TakesStage>) {
        stages.clear()
        stages.addAll(newStages)
        generateFragments()
        notifyDataSetChanged()
    }

    private fun generateFragments() {
        stages.forEachIndexed { index, takesStage ->
            fragments.add(when (takesStage.type) {
                StageTypes.Lecture.name -> {
                    LectureDisplayFragment.newInstance(index)
                }
                StageTypes.Code.name -> {
                    CodingDisplayFragment.newInstance(index)
                }
                else -> {
                    BlockDiagramDisplayFragment.newInstance(index)
                }
            })
        }
    }
}