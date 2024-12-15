package com.shubham.todo.ui.todo_list

import TodoRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shubham.todo.data.local.Todo


class TodoListViewModel(private val repository: TodoRepository) : ViewModel() {

    val localTodos: LiveData<List<Todo>> = repository.allTodos

    suspend fun getRemoteTodos(): List<Todo> {
        return repository.fetchTodosFromApi()
    }
}
