package com.todos.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "taskId") val id: Int,

    @ColumnInfo(name = "taskName") val name: String,

    @ColumnInfo(name = "taskStatus") val status: Boolean
)