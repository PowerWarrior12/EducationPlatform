package com.example.educationplatform.presentation.cource.modules.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class ModulesAdapter(onModuleClick: (moduleId: Int) -> Unit) : AsyncListDifferDelegationAdapter<ModuleItem>(
        ModuleItemDiffCallback,
        takesModuleAdapter(onModuleClick)
)