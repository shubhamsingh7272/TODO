import androidx.lifecycle.LiveData
import com.shubham.todo.data.local.Todo
import com.shubham.todo.data.local.TodoDao
import com.shubham.todo.data.remote.TodoApiService

class TodoRepository(
    private val todoDao: TodoDao,
    private val apiService: TodoApiService
) {
    val allTodos: LiveData<List<Todo>> = todoDao.getAllTodos()

    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    suspend fun delete(todo: Todo) {
        todoDao.deleteById(todo.id) // Use the ID of the TODO
    }

    suspend fun deleteAll() {
        todoDao.deleteAll()
    }

    suspend fun fetchTodosFromApi(): List<Todo> {
        return apiService.getTodos()
    }
}
