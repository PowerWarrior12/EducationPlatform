package com.example.educationplatform.presentation.courseEdit.structure.adapter

import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.educationplatform.R
import com.example.educationplatform.databinding.StructureBlockItemBinding
import com.example.educationplatform.presentation.courseEdit.structure.StageTypes
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun moduleAdapter(onClick: (moduleId: Int, courseId: Int) -> Unit, onDeleteClick: (moduleId: Int) -> Unit) =
    adapterDelegateViewBinding<StructureBlockItem.ModuleItem, StructureBlockItem, StructureBlockItemBinding>(
        { layoutInflater, root -> StructureBlockItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.apply {
                icon.visibility = View.GONE
                titleText.text = item.module.title
                actionButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        root.resources,
                        R.drawable.baseline_remove_circle_outline,
                        root.context.theme
                    )
                )
                actionButton.setOnClickListener {
                    onDeleteClick(item.module.id)
                }
                root.setOnClickListener {
                    onClick(item.module.id, item.module.courseId)
                }

                root.alpha = 1f
            }
        }
    }

fun stageAdapter(onClick: (stageId: Int, moduleId: Int, stageType: String) -> Unit, onDeleteClick: (stageId: Int) -> Unit) =
    adapterDelegateViewBinding<StructureBlockItem.StageItem, StructureBlockItem, StructureBlockItemBinding>(
        { layoutInflater, root -> StructureBlockItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.apply {
                icon.visibility = View.VISIBLE
                val drawableId = when (item.stage.type) {
                    StageTypes.Lecture.name -> {
                        R.drawable.lecture_icon
                    }
                    StageTypes.Code.name -> {
                        R.drawable.code_icon
                    }
                    else -> {
                        R.drawable.block_diagram_icon
                    }
                }

                icon.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        root.resources,
                        drawableId,
                        root.context.theme
                    )
                )

                titleText.text = item.stage.title
                actionButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        root.resources,
                        R.drawable.baseline_remove_circle_outline,
                        root.context.theme
                    )
                )
                actionButton.setOnClickListener {
                    onDeleteClick(item.stage.id)
                }
                root.setOnClickListener {
                    onClick(item.stage.id, item.stage.moduleId, item.stage.type)
                }

                root.alpha = 1f
            }
        }
    }

fun addModuleAdapter(onAddClick: (courseId: Int) -> Unit) =
    adapterDelegateViewBinding<StructureBlockItem.AddModuleItem, StructureBlockItem, StructureBlockItemBinding>(
        { layoutInflater, root -> StructureBlockItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.apply {
                icon.visibility = View.GONE
                titleText.text = root.resources.getString(R.string.add_module_label)
                actionButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        root.resources,
                        R.drawable.baseline_add_box,
                        root.context.theme
                    )
                )
                actionButton.setOnClickListener {
                    onAddClick(item.courseId)
                }
                root.alpha = 0.5f
            }
        }
    }

fun addStageAdapter(onAddClick: (moduleId: Int) -> Unit) =
    adapterDelegateViewBinding<StructureBlockItem.AddStageItem, StructureBlockItem, StructureBlockItemBinding>(
        { layoutInflater, root -> StructureBlockItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.apply {
                icon.visibility = View.GONE
                titleText.text = root.resources.getString(R.string.add_stage_label)
                actionButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        root.resources,
                        R.drawable.baseline_add_box,
                        root.context.theme
                    )
                )
                actionButton.setOnClickListener {
                    onAddClick(item.moduleId)
                }
                root.alpha = 0.5f
            }
        }
    }