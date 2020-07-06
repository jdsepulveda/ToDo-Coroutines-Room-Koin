package com.todos.local.database

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.todos.local.model.Task

@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
abstract class ToDoDB : RoomDatabase() {

    companion object {
        private val LOCK = Any()
        private const val DATABASE_NAME = "todo.db"
        @Volatile
        private var INSTANCE: ToDoDB? = null

        fun getInstance(@NonNull context: Context): ToDoDB {
            if (INSTANCE == null) {
                synchronized(LOCK) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context,
                            ToDoDB::class.java,
                            DATABASE_NAME
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    abstract fun getTaskDao(): TaskDAO
}