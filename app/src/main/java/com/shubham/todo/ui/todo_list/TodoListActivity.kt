package com.shubham.todo.ui.todo_list

import TodoListViewModelFactory
import TodoRepository
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubham.todo.R
import com.shubham.todo.data.local.TodoDatabase
import com.shubham.todo.data.remote.TodoApiService
import com.shubham.todo.databinding.ActivityTodoListBinding
import com.shubham.todo.ui.main.MainActivity
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TodoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoListBinding
    private lateinit var viewModel: TodoListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = TodoDatabase.getDatabase(this)
        val apiService = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoApiService::class.java)
        val repository = TodoRepository(database.todoDao(), apiService)

        val factory = TodoListViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TodoListViewModel::class.java]

        setupRecyclerView()

        viewModel.localTodos.observe(this) { todos ->
            (binding.rvTodoList.adapter as TodoAdapter).submitList(todos)
        }

        lifecycleScope.launch {
            try {
                val remoteTodos = viewModel.getRemoteTodos()
                (binding.rvTodoList.adapter as TodoAdapter).addRemoteTodos(remoteTodos)
            } catch (e: Exception) {
                Toast.makeText(this@TodoListActivity, "Failed to fetch remote data", Toast.LENGTH_SHORT).show()
            }
        }

        // Bottom navigation handling
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_todo_list -> true // Already on TodoListActivity
                R.id.nav_create_todo -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        LinearLayoutManager(this).also { binding.rvTodoList.layoutManager = it }
        binding.rvTodoList.adapter = TodoAdapter()
    }
}
