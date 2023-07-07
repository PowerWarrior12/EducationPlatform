package com.example.educationplatform.data.local.user.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = -1,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "second_name")
    val secondName: String,
    @ColumnInfo(name = "icon")
    val icon: String = "",
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "info")
    val info: String,
    @ColumnInfo(name = "status")
    val status: String
)