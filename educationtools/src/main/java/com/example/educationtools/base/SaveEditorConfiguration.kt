package com.example.educationtools.base

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SaveEditorConfiguration(
    @Json(name = "blocks")
    val blocks: List<EditableBlockFactory<EditableBlockBase>>
)