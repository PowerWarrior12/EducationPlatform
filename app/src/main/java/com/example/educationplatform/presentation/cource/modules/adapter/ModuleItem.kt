package com.example.educationplatform.presentation.cource.modules.adapter

import com.example.educationplatform.domain.entities.TakesModule

sealed class ModuleItem {
    data class TakesModuleItem(val module: TakesModule, val checkable: Boolean): ModuleItem()
}
