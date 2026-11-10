package com.caolehuyhoang.tuan5

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caolehuyhoang.tuan5.ui.theme.Line
import com.caolehuyhoang.tuan5.ui.theme.TextMain
import com.caolehuyhoang.tuan5.ui.theme.TextSub

private val filters = listOf("" to "Tất cả", "study" to "Học tập", "life" to "Cuộc sống")

@Composable
fun ListScreen(vm: TaskViewModel, onOpen: (Int) -> Unit) {
    val state by vm.tasks.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf("") }

    LoadContent(state, onRetry = vm::loadTasks) { tasks ->
        val shown = tasks
            .filter { filter.isEmpty() || it.category == filter }
            .filter { it.title.contains(query.trim(), ignoreCase = true) }
            .sortedWith(compareBy({ it.date }, { it.time }))
        val todo = shown.filter { !it.done }
        val done = shown.filter { it.done }

        LazyColumn(
            Modifier.statusBarsPadding(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Danh sách", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("${tasks.count { !it.done }} việc chưa xong", fontSize = 12.sp, color = TextSub)
                }
            }
            item {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Tìm công việc") },
                    leadingIcon = { Icon(Icons.Outlined.Search, null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Line,
                        unfocusedContainerColor = Line,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    filters.forEach { (key, label) ->
                        FilterChip(
                            selected = filter == key,
                            onClick = { filter = key },
                            label = { Text(label) },
                            shape = RoundedCornerShape(50),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White,
                                selectedContainerColor = TextMain,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
            if (shown.isEmpty()) item { Text("Không có công việc nào.", color = TextSub) }
            if (todo.isNotEmpty()) item { Section("CẦN LÀM") }
            items(todo, key = { it.id }) { task ->
                TaskRow(task, dateLabel(task), { vm.toggleDone(task) }, { onOpen(task.id) }, showUrgent = true)
            }
            if (done.isNotEmpty()) item { Section("ĐÃ XONG") }
            items(done, key = { it.id }) { task ->
                TaskRow(task, dateLabel(task), { vm.toggleDone(task) }, { onOpen(task.id) })
            }
        }
    }
}

@Composable
private fun Section(text: String) {
    Text(
        text,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextSub,
        modifier = Modifier.padding(top = 6.dp)
    )
}
