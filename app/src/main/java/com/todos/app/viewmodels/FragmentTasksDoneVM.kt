package com.todos.app.viewmodels

import androidx.lifecycle.*
import com.todos.R
import com.todos.app.utils.Event
import com.todos.app.utils.EventTypes
import com.todos.app.utils.Resource
import com.todos.app.utils.ResourceString
import com.todos.local.model.Task
import com.todos.source.LocalDataSource
import kotlinx.coroutines.launch
import javax.inject.Inject

class FragmentTasksDoneVM @Inject constructor(
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
                taskList.postValue(Resource.success(localDataSource.getTasks(true)))
            } catch (e: Throwable) {
                taskList.postValue(Resource.error(e.localizedMessage))
            }
        }
    }

    fun updateTaskStatus(task: Task) {
        viewModelScope.launch {
            try {
                localDataSource.updateTask(task)
                taskList.postValue(Resource.success(localDataSource.getTasks(true)))
                eventTypes.value = Event(EventTypes.ShowMsgContextString(ResourceString(R.string.task_updated)))
            } catch (e: Throwable) {
                eventTypes.value = Event(EventTypes.ShowErrorContextString(ResourceString(R.string.error_msg)))
            }
        }
    }
}