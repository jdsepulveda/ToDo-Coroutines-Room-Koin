package com.todos.app.di

import com.todos.app.viewmodels.DialogFragmentTaskVM
import com.todos.app.viewmodels.FragmentTasksDoneVM
import com.todos.app.viewmodels.FragmentTasksListVM
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { FragmentTasksListVM(get()) }
    viewModel { FragmentTasksDoneVM(get()) }
    viewModel { DialogFragmentTaskVM(get()) }
}