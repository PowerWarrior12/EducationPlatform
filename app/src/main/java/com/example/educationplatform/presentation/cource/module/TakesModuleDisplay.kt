package com.example.educationplatform.presentation.cource.module

import com.example.educationplatform.domain.entities.TakesStage

data class TakesModuleDisplay(
    val id: Int,
    val title: String,
    val stages: List<TakesStage>
)