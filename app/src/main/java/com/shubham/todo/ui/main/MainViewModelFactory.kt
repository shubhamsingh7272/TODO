package com.shubham.todo.ui.main

import TodoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shubham.todo.data.local.TodoDao
import com.shubham.todo.data.remote.TodoApiService

class MainViewModelFactory(
    private val todoDao: TodoDao,
    private val apiService: TodoApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val repository = TodoRepository(todoDao, apiService)
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
