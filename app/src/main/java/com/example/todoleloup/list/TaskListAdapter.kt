package com.example.todoleloup.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoleloup.databinding.ItemTaskBinding

object TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldTask: Task, newTask: Task) : Boolean {
        return oldTask.id == newTask.id
    }

    override fun areContentsTheSame(oldTask: Task, newTask: Task) : Boolean {
        return oldTask == newTask
    }
}

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskDiffCallback) {

    var onClickDelete: (Task) -> Unit = {}
    var onClickEdit: (Task) -> Unit = {}

    class TaskViewHolder(
        private val binding: ItemTaskBinding,
        val deleteListener: (Task) -> Unit,
        val editListener: (Task) -> Unit)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description
            binding.deleteButton.setOnClickListener {
                deleteListener(task)
            }
            binding.editButton.setOnClickListener {
                editListener(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(layoutInflater, parent, false)
        return TaskViewHolder(binding, onClickDelete, onClickEdit)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }
}