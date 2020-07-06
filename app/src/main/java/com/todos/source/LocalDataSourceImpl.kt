package com.todos.source

import androidx.lifecycle.LiveData
import com.todos.local.database.TaskDAO
import com.todos.local.model.Task
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val taskDAO: TaskDAO
) : LocalDataSource {

    override suspend fun addTask(task: Task) {
        taskDAO.addTask(task)
    }

    override suspend fun updateTask(task: Task) {
        taskDAO.updateTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        taskDAO.deleteTask(task)
    }

    override suspend fun getTasks(status: Boolean): List<Task> {
        return taskDAO.getTasks(status)
    }

    override fun getTask(id: Int): LiveData<Task> {
        return taskDAO.getTask(id)
    }

    override suspend fun deleteAllTasks() {
        taskDAO.deleteAllTasks()
    }
}