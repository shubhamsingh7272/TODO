package com.shubham.todo.ui.main

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shubham.todo.R
import com.shubham.todo.data.local.Todo
import com.shubham.todo.data.local.TodoDatabase
import com.shubham.todo.data.remote.TodoApiService
import com.shubham.todo.databinding.ActivityMainBinding
import com.shubham.todo.ui.todo_list.TodoListActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var dueDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database and API service
        val database = TodoDatabase.getDatabase(application)
        val todoDao = database.todoDao()
        val apiService = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/") // Replace with your base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoApiService::class.java)

        // Initialize ViewModel with factory
        val factory = MainViewModelFactory(todoDao, apiService)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        // Bottom Navigation Setup
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_create_todo -> {
                    // Current activity - no action needed
                    true
                }
                R.id.nav_todo_list -> {
                    // Navigate to TodoListActivity
                    val intent = Intent(this, TodoListActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Date and Time Picker
        binding.btnPickDateTime.setOnClickListener {
            showDateTimePicker()
        }

        // Save TODO functionality
        binding.btnSave.setOnClickListener {
            val title = binding.etTodoTitle.text.toString()
            val description = binding.etTodoDescription.text.toString()

            if (title.isNotBlank() && description.isNotBlank() && dueDate.isNotBlank()) {
                try {
                    val todo = Todo(
                        title = title,
                        description = description,
                        dueDate = dueDate
                    )
                    viewModel.addTodo(todo)
                    Toast.makeText(this, "TODO saved!", Toast.LENGTH_SHORT).show()

                } catch (e: Exception) {
                    Toast.makeText(this, "Error saving TODO: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("MainActivity", "Error saving todo", e)
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                dueDate = dateFormat.format(calendar.time)
                binding.tvDueDate.text = "Due Date: $dueDate"
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.show()
    }
}
