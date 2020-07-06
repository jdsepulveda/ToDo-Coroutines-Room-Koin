package com.todos.local.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.todos.local.model.Task

@Dao
interface TaskDAO {

    @Insert
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task_table WHERE taskStatus = :status")
    suspend fun getTasks(status: Boolean): List<Task>

    @Query("SELECT * FROM task_table WHERE taskId = :id")
    fun getTask(id: Int): LiveData<Task>

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTasks()
}