package com.shubham.todo.ui.main

import TodoRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.todo.data.local.Todo
import kotlinx.coroutines.launch

class MainViewModel(private val repository: TodoRepository) : ViewModel() {

    val todos = repository.allTodos

    fun addTodo(todo: Todo) {
        viewModelScope.launch {
            try {
                repository.insert(todo) // assuming this is a suspend function
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error adding todo", e)
            }
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }
}
