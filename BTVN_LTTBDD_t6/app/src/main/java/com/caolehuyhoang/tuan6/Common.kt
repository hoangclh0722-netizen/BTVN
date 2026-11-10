package com.caolehuyhoang.tuan6

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caolehuyhoang.tuan6.ui.theme.Blue
import com.caolehuyhoang.tuan6.ui.theme.BlueLight
import com.caolehuyhoang.tuan6.ui.theme.Line
import com.caolehuyhoang.tuan6.ui.theme.Orange
import com.caolehuyhoang.tuan6.ui.theme.OrangeLight
import com.caolehuyhoang.tuan6.ui.theme.Red
import com.caolehuyhoang.tuan6.ui.theme.RedLight
import com.caolehuyhoang.tuan6.ui.theme.TextSub
import java.time.temporal.ChronoUnit

val CardShape = RoundedCornerShape(14.dp)

fun categoryName(category: String) = if (category == "study") "Học tập" else "Cuộc sống"

fun dateLabel(task: Task): String {
    val days = ChronoUnit.DAYS.between(TODAY, task.localDate)
    val day = when {
        days == 0L -> "Hôm nay"
        days == 1L -> "Ngày mai"
        days in 2..6 -> "Tuần này"
        else -> "%02d/%02d".format(task.localDate.dayOfMonth, task.localDate.monthValue)
    }
    return "$day, ${task.time}"
}

@Composable
fun Tag(text: String, color: Color, background: Color) {
    Text(
        text,
        color = color,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(background, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Composable
fun CategoryTag(category: String) {
    if (category == "study") Tag("Học tập", Blue, BlueLight)
    else Tag("Cuộc sống", Orange, OrangeLight)
}

@Composable
fun CheckCircle(checked: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .size(24.dp)
            .clip(CircleShape)
            .then(if (checked) Modifier.background(Blue) else Modifier.border(1.5.dp, Line, CircleShape))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (checked) Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
    }
}

@Composable
fun TaskRow(task: Task, subtitle: String, onToggle: () -> Unit, onClick: () -> Unit, showUrgent: Boolean = false) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CheckCircle(task.done, onToggle)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                task.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (task.done) TextSub else Color.Unspecified,
                textDecoration = if (task.done) TextDecoration.LineThrough else null,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!task.done) {
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CategoryTag(task.category)
                    Spacer(Modifier.width(8.dp))
                    Text(subtitle, fontSize = 12.sp, color = TextSub)
                }
            }
        }
        if (showUrgent && !task.done && task.priority == "high") Tag("Gấp", Red, RedLight)
        else Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Line)
    }
}

@Composable
fun <T> LoadContent(state: Load<T>, onRetry: () -> Unit, content: @Composable (T) -> Unit) {
    when (state) {
        is Load.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        is Load.Error -> Column(
            Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Lỗi: ${state.message}", textAlign = TextAlign.Center, color = Red)
            Spacer(Modifier.height(12.dp))
            Button(onClick = onRetry) { Text("Thử lại") }
        }
        is Load.Ok -> content(state.data)
    }
}
