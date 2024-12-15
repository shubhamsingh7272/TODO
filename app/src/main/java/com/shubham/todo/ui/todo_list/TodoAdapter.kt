package com.shubham.todo.ui.todo_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shubham.todo.data.local.Todo
import com.shubham.todo.databinding.ItemTodoBinding

class TodoAdapter : ListAdapter<Todo, TodoAdapter.TodoViewHolder>(TodoDiffCallback()) {

    private val remoteTodos = mutableListOf<Todo>()

    inner class TodoViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            binding.tvTitle.text = todo.title
            binding.tvDescription.text = todo.description
            binding.tvDueDate.text = todo.dueDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun addRemoteTodos(todos: List<Todo>) {
        remoteTodos.clear()
        remoteTodos.addAll(todos)
        submitList(currentList + remoteTodos)
    }
}

class TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean = oldItem == newItem
}
