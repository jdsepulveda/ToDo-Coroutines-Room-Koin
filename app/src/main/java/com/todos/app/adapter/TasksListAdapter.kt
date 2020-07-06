package com.todos.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.todos.R
import com.todos.databinding.TaskListItemBinding
import com.todos.local.model.Task

class TasksListAdapter(
    private val clickListener: (Task) -> Unit
) : RecyclerView.Adapter<TasksListVH>() {

    private val tasksList: MutableList<Task> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksListVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : TaskListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.task_list_item, parent,false)
        return TasksListVH(binding)
    }

    override fun getItemCount() = tasksList.size

    override fun onBindViewHolder(holder: TasksListVH, position: Int) {
        holder.bind(tasksList[position], clickListener)
    }

    fun populate(tasks: List<Task>) {
        tasksList.clear()
        tasksList.addAll(tasks)
        notifyDataSetChanged()
    }
}

class TasksListVH(private val binding: TaskListItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(task: Task, clickListener:(Task)->Unit) {
        binding.chkTaskName.text = task.name
        binding.chkTaskName.isChecked = task.status
        binding.chkTaskName.setOnClickListener {
            val taskUpdated = task.copy(status = !task.status)
            clickListener(taskUpdated)
        }
    }
}