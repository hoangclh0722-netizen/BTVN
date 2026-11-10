package com.caolehuyhoang.tuan6

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import java.time.LocalDate

data class Subtask(val title: String = "", val done: Boolean = false)

data class Task(
    val id: Int = 0,
    val title: String = "",
    val note: String = "",
    val category: String = "",
    val date: String = "",
    val time: String = "",
    val priority: String = "",
    val remind: Boolean = false,
    val done: Boolean = false,
    val subtasks: List<Subtask> = emptyList()
) {
    val localDate: LocalDate get() = LocalDate.parse(date)
}

interface TaskApi {
    @GET("mock/teach/tasks")
    suspend fun getTasks(): List<Task>

    @GET("mock/teach/tasks/{id}")
    suspend fun getTask(@Path("id") id: Int): Task

    @PATCH("mock/teach/tasks/{id}")
    suspend fun updateTask(@Path("id") id: Int, @Body body: Map<String, @JvmSuppressWildcards Any>): Response<Unit>

    @DELETE("mock/teach/tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Int): Response<Unit>
}

object Api {
    val tasks: TaskApi = Retrofit.Builder()
        .baseUrl("https://share.tryapps.store/")
        .client(
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    chain.proceed(
                        chain.request().newBuilder()
                            .header("Authorization", "Bearer ${BuildConfig.MOCK_API_TOKEN}")
                            .build()
                    )
                }
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TaskApi::class.java)
}

sealed interface Load<out T> {
    data object Loading : Load<Nothing>
    data class Ok<T>(val data: T) : Load<T>
    data class Error(val message: String) : Load<Nothing>
}

// Dữ liệu mẫu của API đều nằm quanh ngày 20/10/2026 nên lấy ngày này làm "hôm nay".
val TODAY: LocalDate = LocalDate.of(2026, 10, 20)

// API mock không lưu thay đổi, nên app giữ bản sửa ở máy để các màn khớp nhau.
class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<Load<List<Task>>>(Load.Loading)
    val tasks: StateFlow<Load<List<Task>>> = _tasks

    private val _detail = MutableStateFlow<Load<Task>>(Load.Loading)
    val detail: StateFlow<Load<Task>> = _detail

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val edited = mutableSetOf<Int>()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _tasks.value = Load.Loading
            _tasks.value = try {
                Load.Ok(Api.tasks.getTasks())
            } catch (e: Exception) {
                Load.Error(e.message ?: "Không tải được danh sách")
            }
        }
    }

    fun openTask(id: Int) {
        viewModelScope.launch {
            val local = localTask(id)
            if (id in edited && local != null) {
                _detail.value = Load.Ok(local)
                return@launch
            }
            _detail.value = Load.Loading
            _detail.value = try {
                Load.Ok(Api.tasks.getTask(id))
            } catch (e: Exception) {
                Load.Error(e.message ?: "Không tải được công việc")
            }
        }
    }

    fun toggleDone(task: Task) = save(task.copy(done = !task.done), mapOf("done" to !task.done))

    fun setSubtasks(task: Task, subtasks: List<Subtask>) =
        save(task.copy(subtasks = subtasks), mapOf("subtasks" to subtasks))

    fun delete(task: Task, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                val res = Api.tasks.deleteTask(task.id)
                if (!res.isSuccessful) error("HTTP ${res.code()}")
                _tasks.update { s -> if (s is Load.Ok) Load.Ok(s.data.filter { it.id != task.id }) else s }
                _message.value = "Đã xóa \"${task.title}\""
                onDone()
            } catch (e: Exception) {
                _message.value = "Xóa thất bại: ${e.message}"
            }
        }
    }

    fun showMessage(text: String) {
        _message.value = text
    }

    fun messageShown() {
        _message.value = null
    }

    private fun localTask(id: Int) = (_tasks.value as? Load.Ok)?.data?.find { it.id == id }

    private fun save(newTask: Task, body: Map<String, Any>) {
        val oldList = _tasks.value
        val oldDetail = _detail.value
        apply(newTask)
        viewModelScope.launch {
            try {
                val res = Api.tasks.updateTask(newTask.id, body)
                if (!res.isSuccessful) error("HTTP ${res.code()}")
                edited += newTask.id
            } catch (e: Exception) {
                _tasks.value = oldList
                _detail.value = oldDetail
                _message.value = "Cập nhật thất bại: ${e.message}"
            }
        }
    }

    private fun apply(task: Task) {
        _tasks.update { s ->
            if (s is Load.Ok) Load.Ok(s.data.map { if (it.id == task.id) task else it }) else s
        }
        _detail.update { s -> if (s is Load.Ok && s.data.id == task.id) Load.Ok(task) else s }
    }
}
