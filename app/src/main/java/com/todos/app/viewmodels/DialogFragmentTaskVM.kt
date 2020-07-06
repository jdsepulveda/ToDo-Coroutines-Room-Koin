package com.todos.app.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todos.R
import com.todos.app.ui.FragmentDialogTaskDirections
import com.todos.app.utils.*
import com.todos.local.model.Task
import com.todos.source.LocalDataSource
import kotlinx.coroutines.launch
import javax.inject.Inject

class DialogFragmentTaskVM @Inject constructor(
    private val localDataSource: LocalDataSource
) : ViewModel() {

    var taskName = MutableLiveData<String>()

    private val eventTypes = MutableLiveData<Event<EventTypes>>()
    val event: LiveData<Event<EventTypes>>
        get() = eventTypes

    val onCreateTaskClick = View.OnClickListener {
        eventTypes.value = Event(EventTypes.CloseKeyboard(it))

        if (taskName.value.isNullOrBlank()) {
            eventTypes.value = Event(EventTypes.ShowErrorContextString(ResourceString(R.string.task_name_required)))
        } else {
            viewModelScope.launch {
                try {
                    localDataSource.addTask(Task(0, taskName.value.toString(), false))
                    eventTypes.value =
                        Event(EventTypes.ShowMsgContextString(ResourceString(R.string.task_saved)))
                    eventTypes.value =
                        Event(EventTypes.Navigate(FragmentDialogTaskDirections.actionFragmentDialogTaskToFragmentTasksList()))
                } catch (e: Throwable) {
                    eventTypes.value =
                        Event(EventTypes.ShowErrorContextString(ResourceString(R.string.error_msg)))
                }
            }
        }
    }
}