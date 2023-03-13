package com.example.educationtools.base

import com.example.educationtools.connection.ConnectionLine
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SaveEditorConfiguration(
    @Json(name = "blocks")
    val blocks: List<EditableBlockFactory>,
    @Json(name = "connections")
    val connectionLines: List<ConnectionLine.Configurations>
)