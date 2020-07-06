package com.todos.app.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.todos.R
import com.todos.app.ui.FragmentTasksListDirections
import com.todos.app.utils.Event
import com.todos.app.utils.EventTypes
import com.todos.app.utils.Resource
import com.todos.app.utils.ResourceString
import com.todos.local.model.Task
import com.todos.source.LocalDataSource
import kotlinx.coroutines.launch
import javax.inject.Inject

class FragmentTasksListVM @Inject constructor(
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private val taskList = MutableLiveData<Resource<List<Task>>>()
    val tasks: LiveData<Resource<List<Task>>>
        get() = taskList

    private val eventTypes = MutableLiveData<Event<EventTypes>>()
    val event: LiveData<Event<EventTypes>>
        get() = eventTypes

    init {
        taskList.postValue(Resource.loading())
        viewModelScope.launch {
            try {
                taskList.postValue(Resource.success(localDataSource.getTasks(false)))
            } catch (e: Throwable) {
                taskList.postValue(Resource.error(e.localizedMessage))
            }
        }
    }

    val onCreateTaskClick = View.OnClickListener {
        it.findNavController().navigate(FragmentTasksListDirections.actionFragmentTasksListToFragmentDialogTask())
    }

    fun updateTaskStatus(task: Task) {
        viewModelScope.launch {
            try {
                localDataSource.updateTask(task)
                taskList.postValue(Resource.success(localDataSource.getTasks(false)))
                eventTypes.value = Event(EventTypes.ShowMsgContextString(ResourceString(R.string.task_updated)))
            } catch (e: Throwable) {
                eventTypes.value = Event(EventTypes.ShowErrorContextString(ResourceString(R.string.error_msg)))
            }
        }
    }
}