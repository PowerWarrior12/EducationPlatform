package com.example.educationplatform.presentation.courseEdit.structure.adapter

import com.example.educationplatform.domain.entities.Module
import com.example.educationplatform.domain.entities.Stage

sealed class StructureBlockItem {
    class ModuleItem(val module: Module): StructureBlockItem()
    class StageItem(val stage: Stage): StructureBlockItem()
    class AddModuleItem(val courseId: Int): StructureBlockItem()
    class AddStageItem(val moduleId: Int): StructureBlockItem()
}