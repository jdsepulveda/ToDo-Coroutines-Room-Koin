package com.todos.source

import androidx.lifecycle.LiveData
import com.todos.local.model.Task

interface LocalDataSource {

    suspend fun addTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun getTasks(status: Boolean): List<Task>

    fun getTask(id: Int): LiveData<Task>

    suspend fun deleteAllTasks()
}