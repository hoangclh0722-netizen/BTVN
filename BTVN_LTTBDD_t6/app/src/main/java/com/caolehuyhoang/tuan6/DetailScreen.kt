package com.caolehuyhoang.tuan6

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caolehuyhoang.tuan6.ui.theme.Blue
import com.caolehuyhoang.tuan6.ui.theme.Line
import com.caolehuyhoang.tuan6.ui.theme.Red
import com.caolehuyhoang.tuan6.ui.theme.RedLight
import com.caolehuyhoang.tuan6.ui.theme.TextSub

@Composable
fun DetailScreen(id: Int, vm: TaskViewModel, backLabel: String, onBack: () -> Unit) {
    val state by vm.detail.collectAsState()
    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Row(
            Modifier.padding(horizontal = 8.dp, vertical = 8.dp).clickable(onClick = onBack),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Quay lại", tint = Blue)
            Text(backLabel, color = Blue)
        }
        LoadContent(state, onRetry = { vm.openTask(id) }) { task ->
            DetailContent(task, vm, onBack)
        }
    }
}

@Composable
private fun DetailContent(task: Task, vm: TaskViewModel, onBack: () -> Unit) {
    var newSub by remember { mutableStateOf("") }
    var confirmDelete by remember { mutableStateOf(false) }
    val doneCount = task.subtasks.count { it.done }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryTag(task.category)
            when (task.priority) {
                "high" -> Tag("Ưu tiên cao", Red, RedLight)
                "medium" -> Tag("Ưu tiên vừa", TextSub, Line)
                else -> Tag("Ưu tiên thấp", TextSub, Line)
            }
        }
        Text(
            task.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textDecoration = if (task.done) TextDecoration.LineThrough else null
        )

        Column(Modifier.background(Color.White, CardShape)) {
            InfoRow(Icons.Outlined.CalendarToday, "Hạn chót", dateLabel(task))
            HorizontalDivider(color = Line)
            InfoRow(Icons.Outlined.NotificationsNone, "Nhắc nhở", if (task.remind) "Trước 15 phút" else "Tắt")
        }

        if (task.note.isNotBlank()) {
            Label("Ghi chú")
            Text(task.note, modifier = Modifier.fillMaxWidth().background(Color.White, CardShape).padding(14.dp))
        }

        Row {
            Label("Việc nhỏ", Modifier.weight(1f))
            Text("$doneCount/${task.subtasks.size}", fontSize = 12.sp, color = TextSub)
        }
        if (task.subtasks.isNotEmpty()) {
            LinearProgressIndicator(
                progress = { doneCount.toFloat() / task.subtasks.size },
                modifier = Modifier.fillMaxWidth(),
                color = Blue,
                trackColor = Line,
                drawStopIndicator = {}
            )
        }
        Column(Modifier.background(Color.White, CardShape)) {
            task.subtasks.forEachIndexed { i, sub ->
                Row(Modifier.padding(start = 4.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(sub.done, onCheckedChange = {
                        vm.setSubtasks(task, task.subtasks.mapIndexed { j, s -> if (j == i) s.copy(done = it) else s })
                    })
                    Text(
                        sub.title,
                        modifier = Modifier.weight(1f),
                        color = if (sub.done) TextSub else Color.Unspecified,
                        textDecoration = if (sub.done) TextDecoration.LineThrough else null
                    )
                    Icon(
                        Icons.Outlined.DeleteOutline, "Xóa việc nhỏ", tint = Red,
                        modifier = Modifier.padding(10.dp).size(20.dp).clickable {
                            vm.setSubtasks(task, task.subtasks.filterIndexed { j, _ -> j != i })
                        }
                    )
                }
                HorizontalDivider(color = Line)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = newSub,
                    onValueChange = { newSub = it },
                    placeholder = { Text("Thêm việc nhỏ") },
                    leadingIcon = { Icon(Icons.Filled.Add, null, tint = Blue) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.weight(1f)
                )
                TextButton(
                    enabled = newSub.isNotBlank(),
                    onClick = {
                        vm.setSubtasks(task, task.subtasks + Subtask(newSub.trim()))
                        newSub = ""
                    }
                ) { Text("Thêm") }
            }
        }

        Spacer(Modifier.height(8.dp))
        if (task.done) {
            OutlinedButton(
                onClick = { vm.toggleDone(task) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Đánh dấu chưa xong") }
        } else {
            Button(
                onClick = { vm.toggleDone(task) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue)
            ) { Text("Đánh dấu hoàn thành", fontWeight = FontWeight.SemiBold) }
        }
        TextButton(onClick = { confirmDelete = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Xóa công việc", color = Red)
        }
        Spacer(Modifier.height(16.dp))
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Xóa công việc?") },
            text = { Text("\"${task.title}\" sẽ bị xóa.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmDelete = false
                    vm.delete(task, onBack)
                }) { Text("Xóa", color = Red) }
            },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Hủy") } }
        )
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = TextSub, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(10.dp))
        Text(label, color = TextSub, modifier = Modifier.weight(1f))
        Text(value, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun Label(text: String, modifier: Modifier = Modifier) {
    Text(text, fontSize = 12.sp, color = TextSub, fontWeight = FontWeight.SemiBold, modifier = modifier)
}
