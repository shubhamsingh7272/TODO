package com.shubham.todo.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllTodos(): LiveData<List<Todo>> // This returns all the TODOs from the database

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)
    // Delete a specific TODO by ID
    @Query("DELETE FROM todo_table WHERE id = :todoId")
    suspend fun deleteById(todoId: Int)

    // Delete all TODOs
    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()
}
