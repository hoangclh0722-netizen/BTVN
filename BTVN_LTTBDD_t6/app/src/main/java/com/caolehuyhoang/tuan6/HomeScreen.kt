package com.caolehuyhoang.tuan6

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caolehuyhoang.tuan6.ui.theme.Blue
import com.caolehuyhoang.tuan6.ui.theme.BlueLight
import com.caolehuyhoang.tuan6.ui.theme.Orange
import com.caolehuyhoang.tuan6.ui.theme.OrangeLight
import com.caolehuyhoang.tuan6.ui.theme.TextSub
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen(vm: TaskViewModel, onOpen: (Int) -> Unit, onSeeAll: () -> Unit) {
    val state by vm.tasks.collectAsState()
    LoadContent(state, onRetry = vm::loadTasks) { tasks ->
        val today = tasks.filter { it.localDate == TODAY }
        LazyColumn(
            Modifier.statusBarsPadding(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Header() }
            item { ProgressCard(today.count { it.done }, today.size) }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CategoryCard(
                        Modifier.weight(1f), "Học tập", Icons.Outlined.School, Blue, BlueLight,
                        tasks.count { it.category == "study" && !it.done }
                    )
                    CategoryCard(
                        Modifier.weight(1f), "Cuộc sống", Icons.Outlined.Home, Orange, OrangeLight,
                        tasks.count { it.category == "life" && !it.done }
                    )
                }
            }
            item {
                Row(Modifier.fillMaxWidth().padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Việc hôm nay", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.weight(1f))
                    Text("Xem tất cả", color = Blue, fontSize = 13.sp, modifier = Modifier.clickable(onClick = onSeeAll))
                }
            }
            if (today.isEmpty()) item { Text("Hôm nay không có việc nào.", color = TextSub) }
            items(today, key = { it.id }) { task ->
                TaskRow(task, task.time, onToggle = { vm.toggleDone(task) }, onClick = { onOpen(task.id) })
            }
        }
    }
}

@Composable
private fun Header() {
    val weekday = TODAY.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("vi"))
        .replaceFirstChar { it.uppercase() }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(42.dp).background(BlueLight, CircleShape), contentAlignment = Alignment.Center) {
            Text("H", color = Blue, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text("$weekday, ${TODAY.dayOfMonth} tháng ${TODAY.monthValue}", fontSize = 12.sp, color = TextSub)
            Text("Chào Hoàng!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Box(Modifier.size(40.dp).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.NotificationsNone, "Thông báo")
        }
    }
}

@Composable
private fun ProgressCard(done: Int, total: Int) {
    val ratio = if (total == 0) 0f else done.toFloat() / total
    val text = when {
        total == 0 -> "Hôm nay rảnh rỗi!"
        done == total -> "Xong hết rồi, giỏi quá!"
        ratio >= 0.5f -> "Đã hơn nửa chặng, cố lên!"
        else -> "Bắt đầu thôi nào!"
    }
    Row(
        Modifier
            .fillMaxWidth()
            .background(Blue, RoundedCornerShape(18.dp))
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text("Tiến độ hôm nay", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            Text("$done/$total việc", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text, color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
        }
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { ratio },
                modifier = Modifier.size(64.dp),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.25f),
                strokeWidth = 6.dp
            )
            Text("${(ratio * 100).toInt()}%", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}

@Composable
private fun CategoryCard(modifier: Modifier, title: String, icon: ImageVector, color: Color, bg: Color, left: Int) {
    Column(
        modifier
            .background(Color.White, CardShape)
            .padding(14.dp)
    ) {
        Box(Modifier.size(34.dp).background(bg, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.height(10.dp))
        Text(title, fontWeight = FontWeight.SemiBold)
        Text("$left việc còn lại", fontSize = 12.sp, color = TextSub)
    }
}
