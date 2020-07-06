package com.todos.app.di

import com.todos.local.database.ToDoDB
import com.todos.source.LocalDataSource
import com.todos.source.LocalDataSourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localSourceModule = module {
    single {
        ToDoDB.getInstance(androidContext())
    }

    single {
        get<ToDoDB>().getTaskDao()
    }

    single<LocalDataSource> {
        LocalDataSourceImpl(get())
    }
}