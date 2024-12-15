package com.shubham.todo.data.remote

import com.shubham.todo.data.local.Todo
import retrofit2.http.GET

interface TodoApiService {
    @GET("/todos")
    suspend fun getTodos(): List<Todo>
}
