package com.todos.app.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.todos.R
import com.todos.app.adapter.TasksListAdapter
import com.todos.app.utils.EventObserver
import com.todos.app.utils.EventTypes
import com.todos.app.utils.Status
import com.todos.app.utils.appBarNavConfiguration
import com.todos.app.utils.extensions.gone
import com.todos.app.utils.extensions.visible
import com.todos.app.viewmodels.FragmentTasksListVM
import com.todos.databinding.FragmentTasksListBinding
import com.todos.local.model.Task
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class FragmentTasksList : Fragment() {

    private val fragmentTasksListVM: FragmentTasksListVM by viewModel()

    private lateinit var tasksListAdapter: TasksListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater, R.layout.fragment_tasks_list, container, false
        ).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataBindingUtil.findBinding<FragmentTasksListBinding>(view)?.apply {
            viewModel = fragmentTasksListVM
            lifecycleOwner = viewLifecycleOwner
        }

        NavigationUI.setupWithNavController(
            tasks_list_toolbar,
            findNavController(),
            appBarNavConfiguration
        )

        initRecyclerView()
        setUpDataObservers()
        setUpObservers()
    }

    private fun initRecyclerView() {
        tasksListAdapter = TasksListAdapter { taskItem: Task -> taskItemClicked(taskItem) }
        rvTaskList.adapter = tasksListAdapter
    }

    private fun taskItemClicked(taskItem: Task) {
        fragmentTasksListVM.updateTaskStatus(taskItem)
    }

    private fun setUpDataObservers() {
        fragmentTasksListVM.tasks.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                Status.LOADING -> {
                    progressBarLoading.visible()
                }
                Status.SUCCESS -> {
                    progressBarLoading.gone()
                    it.data.orEmpty().let { tasks ->
                        Log.d("Task List", tasks.size.toString())
                        tasksListAdapter.populate(tasks)
                    }
                }
                Status.ERROR -> {
                    progressBarLoading.gone()
                }
            }
        })
    }

    private fun setUpObservers() {
        // TODO: Create a way similar to: override fun processEffect(event: ViewEffect) { when (event) { ... } }
        fragmentTasksListVM.event.observe(viewLifecycleOwner, EventObserver { eventTypes ->
            when(eventTypes) {
                is EventTypes.ShowMsgContextString -> {
                    this.view?.let {
                        Snackbar.make(it, eventTypes.msg.format(this.requireContext()), Snackbar.LENGTH_LONG).show()
                    }
                }
                is EventTypes.ShowError -> {
                    this.view?.let {
                        Snackbar.make(it, eventTypes.errorMsg, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}