package com.example.educationplatform.presentation.courseEdit.structure.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class StructureBlocksAdapter(
    onModuleClick: (moduleId: Int, courseId: Int) -> Unit,
    onStageClick: (stageId: Int, moduleId: Int, stageType: String) -> Unit,
    onModuleDelete: (moduleId: Int) -> Unit,
    onStageDelete: (stageId: Int) -> Unit,
    addModule: (courseId: Int) -> Unit,
    addStage: (moduleId: Int) -> Unit
): AsyncListDifferDelegationAdapter<StructureBlockItem>(
    StructureBlockDiffCallback,
    moduleAdapter(onModuleClick, onModuleDelete),
    stageAdapter(onStageClick, onStageDelete),
    addModuleAdapter(addModule),
    addStageAdapter(addStage)
)