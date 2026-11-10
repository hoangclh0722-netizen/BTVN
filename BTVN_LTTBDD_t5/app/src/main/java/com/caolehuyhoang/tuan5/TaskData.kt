package com.caolehuyhoang.tuan5

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
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

    fun toMap() = mapOf(
        "id" to id, "title" to title, "note" to note, "category" to category,
        "date" to date, "time" to time, "priority" to priority, "remind" to remind,
        "done" to done, "subtasks" to subtasks.map { mapOf("title" to it.title, "done" to it.done) }
    )
}

private fun DocumentSnapshot.toTask() = Task(
    id = getLong("id")?.toInt() ?: 0,
    title = getString("title").orEmpty(),
    note = getString("note").orEmpty(),
    category = getString("category").orEmpty(),
    date = getString("date").orEmpty(),
    time = getString("time").orEmpty(),
    priority = getString("priority").orEmpty(),
    remind = getBoolean("remind") ?: false,
    done = getBoolean("done") ?: false,
    subtasks = (get("subtasks") as? List<*>).orEmpty().mapNotNull { item ->
        (item as? Map<*, *>)?.let { Subtask(it["title"] as? String ?: "", it["done"] as? Boolean ?: false) }
    }
)

// API mock tuần 4 chỉ dùng để lấy dữ liệu mẫu lần đầu cho mỗi tài khoản.
interface TaskApi {
    @GET("mock/teach/tasks")
    suspend fun getTasks(): List<Task>
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

// Dữ liệu mẫu đều nằm quanh ngày 20/10/2026 nên lấy ngày này làm "hôm nay".
val TODAY: LocalDate = LocalDate.of(2026, 10, 20)

class TaskViewModel : ViewModel() {
    private val db = Firebase.firestore
    private var uid: String? = null
    private var listener: ListenerRegistration? = null

    private val _tasks = MutableStateFlow<Load<List<Task>>>(Load.Loading)
    val tasks: StateFlow<Load<List<Task>>> = _tasks

    private val _openId = MutableStateFlow<Int?>(null)
    val openId: StateFlow<Int?> = _openId

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private fun userDoc() = db.collection("users").document(uid!!)
    private fun taskDoc(id: Int) = userDoc().collection("tasks").document(id.toString())

    fun start(uid: String) {
        if (this.uid == uid) return
        listener?.remove()
        this.uid = uid
        _tasks.value = Load.Loading
        viewModelScope.launch {
            try {
                seedIfNeeded()
            } catch (e: Exception) {
                Firebase.crashlytics.recordException(e)
                _message.value = "Không lấy được dữ liệu mẫu: ${e.message}"
            }
            listener = userDoc().collection("tasks").addSnapshotListener { snap, error ->
                if (error != null) {
                    Firebase.crashlytics.recordException(error)
                    _tasks.value = Load.Error(error.message ?: "Lỗi Firestore")
                } else if (snap != null) {
                    _tasks.value = Load.Ok(snap.documents.map { it.toTask() })
                }
            }
        }
    }

    fun stop() {
        listener?.remove()
        listener = null
        uid = null
        _openId.value = null
        _tasks.value = Load.Loading
    }

    private suspend fun seedIfNeeded() {
        if (userDoc().get().await().getBoolean("seeded") == true) return
        val batch = db.batch()
        Api.tasks.getTasks().forEach { batch.set(taskDoc(it.id), it.toMap()) }
        batch.set(userDoc(), mapOf("seeded" to true))
        batch.commit().await()
    }

    fun loadTasks() {
        uid?.let {
            uid = null
            start(it)
        }
    }

    fun open(id: Int?) {
        _openId.value = id
    }

    fun toggleDone(task: Task) {
        update(task.id, mapOf("done" to !task.done))
        if (!task.done) Firebase.analytics.logEvent("task_done", null)
    }

    fun setSubtasks(task: Task, subtasks: List<Subtask>) =
        update(task.id, mapOf("subtasks" to subtasks.map { mapOf("title" to it.title, "done" to it.done) }))

    fun delete(task: Task, onDone: () -> Unit) {
        onDone()
        taskDoc(task.id).delete()
            .addOnSuccessListener {
                Firebase.analytics.logEvent("task_deleted", null)
                _message.value = "Đã xóa \"${task.title}\""
            }
            .addOnFailureListener { fail("Xóa thất bại", it) }
    }

    fun showMessage(text: String) {
        _message.value = text
    }

    fun messageShown() {
        _message.value = null
    }

    private fun update(id: Int, fields: Map<String, Any>) {
        taskDoc(id).update(fields).addOnFailureListener { fail("Cập nhật thất bại", it) }
    }

    private fun fail(text: String, e: Exception) {
        Firebase.crashlytics.recordException(e)
        _message.value = "$text: ${e.message}"
    }

    override fun onCleared() {
        listener?.remove()
    }
}
