package com.todos.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.todos.R
import com.todos.databinding.TaskListDoneItemBinding
import com.todos.local.model.Task

class TasksListDoneAdapter(
    private val clickListener: (Task) -> Unit
) : RecyclerView.Adapter<TasksListDoneVH>() {

    private val tasksListDone: MutableList<Task> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksListDoneVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : TaskListDoneItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.task_list_done_item, parent,false)
        return TasksListDoneVH(binding)
    }

    override fun getItemCount() = tasksListDone.size

    override fun onBindViewHolder(holder: TasksListDoneVH, position: Int) {
        holder.bind(tasksListDone[position], clickListener)
    }

    fun populate(tasks: List<Task>) {
        tasksListDone.clear()
        tasksListDone.addAll(tasks)
        notifyDataSetChanged()
    }
}

class TasksListDoneVH(private val binding: TaskListDoneItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(task: Task, clickListener:(Task)->Unit) {
        binding.chkTaskDoneName.text = task.name
        binding.chkTaskDoneName.isChecked = task.status
        binding.chkTaskDoneName.setOnClickListener {
            val taskUpdated = task.copy(status = !task.status)
            clickListener(taskUpdated)
        }
    }
}