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
import com.todos.app.adapter.TasksListDoneAdapter
import com.todos.app.utils.EventObserver
import com.todos.app.utils.EventTypes
import com.todos.app.utils.Status
import com.todos.app.utils.appBarNavConfiguration
import com.todos.app.utils.extensions.gone
import com.todos.app.utils.extensions.visible
import com.todos.app.viewmodels.FragmentTasksDoneVM
import com.todos.databinding.FragmentTasksDoneBinding
import com.todos.local.model.Task
import kotlinx.android.synthetic.main.fragment_tasks_done.*
import org.koin.android.viewmodel.ext.android.viewModel

class FragmentTasksDone : Fragment() {

    private val fragmentTasksDoneVM: FragmentTasksDoneVM by viewModel()

    private lateinit var tasksListDoneAdapter: TasksListDoneAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater, R.layout.fragment_tasks_done, container, false
        ).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataBindingUtil.findBinding<FragmentTasksDoneBinding>(view)?.apply {
            viewModel = fragmentTasksDoneVM
            lifecycleOwner = viewLifecycleOwner
        }

        NavigationUI.setupWithNavController(
            tasks_done_toolbar,
            findNavController(),
            appBarNavConfiguration
        )

        initRecyclerView()
        setUpDataObservers()
        setUpObservers()
    }

    private fun initRecyclerView() {
        tasksListDoneAdapter = TasksListDoneAdapter { taskItem: Task -> taskDoneItemClicked(taskItem) }
        rvTaskListDone.adapter = tasksListDoneAdapter
    }

    private fun taskDoneItemClicked(taskItem: Task) {
        fragmentTasksDoneVM.updateTaskStatus(taskItem)
    }

    private fun setUpDataObservers() {
        fragmentTasksDoneVM.tasks.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                Status.LOADING -> {
                    progressBarLoading.visible()
                }
                Status.SUCCESS -> {
                    progressBarLoading.gone()
                    it.data.orEmpty().let { tasks ->
                        Log.d("Task List", tasks.size.toString())
                        tasksListDoneAdapter.populate(tasks)
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
        fragmentTasksDoneVM.event.observe(viewLifecycleOwner, EventObserver { eventTypes ->
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