package com.caolehuyhoang.tuan5

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
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
import com.caolehuyhoang.tuan5.ui.theme.Blue
import com.caolehuyhoang.tuan5.ui.theme.BlueLight
import com.caolehuyhoang.tuan5.ui.theme.Green
import com.caolehuyhoang.tuan5.ui.theme.GreenLight
import com.caolehuyhoang.tuan5.ui.theme.Line
import com.caolehuyhoang.tuan5.ui.theme.Red
import com.caolehuyhoang.tuan5.ui.theme.TextSub
import com.google.firebase.auth.FirebaseUser
import java.time.DayOfWeek

@Composable
fun ProfileScreen(user: FirebaseUser, vm: TaskViewModel, backLabel: String, onBack: () -> Unit, onSignOut: () -> Unit) {
    val state by vm.tasks.collectAsState()
    val name = user.displayNameOrGuest()

    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Box(Modifier.fillMaxWidth()) {
            BackRow(backLabel, onBack)
            Text("Hồ sơ", fontWeight = FontWeight.SemiBold, modifier = Modifier.align(Alignment.Center))
        }
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(Modifier.size(84.dp).background(BlueLight, CircleShape), contentAlignment = Alignment.Center) {
                Text(initials(name), color = Blue, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(if (user.isAnonymous) "Tài khoản dùng thử" else user.email.orEmpty(), color = TextSub, fontSize = 13.sp)
                Spacer(Modifier.height(6.dp))
                if (user.isAnonymous) Tag("Chưa liên kết tài khoản", TextSub, Line)
                else Row(
                    Modifier.background(GreenLight, RoundedCornerShape(6.dp)).padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Check, null, tint = Green, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Đã liên kết tài khoản Google", color = Green, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            val tasks = (state as? Load.Ok)?.data.orEmpty()
            val done = tasks.count { it.done }
            val percent = if (tasks.isEmpty()) 0 else done * 100 / tasks.size
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard(Modifier.weight(1f), "$done", "Việc đã xong")
                StatCard(Modifier.weight(1f), "${tasks.size - done}", "Đang chờ")
                StatCard(Modifier.weight(1f), "$percent%", "Hoàn thành")
            }
            WeekChart(tasks)

            Spacer(Modifier.height(8.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White, CardShape)
                    .clickable(onClick = onSignOut)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.AutoMirrored.Outlined.Logout, null, tint = Red, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Đăng xuất", color = Red, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier, value: String, label: String) {
    Column(modifier.background(Color.White, CardShape).padding(12.dp)) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 11.sp, color = TextSub)
    }
}

@Composable
private fun WeekChart(tasks: List<Task>) {
    val monday = TODAY.with(DayOfWeek.MONDAY)
    val counts = (0..6).map { i -> tasks.count { it.done && it.localDate == monday.plusDays(i.toLong()) } }
    val max = counts.max().coerceAtLeast(1)
    val labels = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")

    Column(Modifier.fillMaxWidth().background(Color.White, CardShape).padding(16.dp)) {
        Row {
            Text("Tuần này", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Text("Việc xong mỗi ngày", fontSize = 12.sp, color = TextSub)
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth().height(110.dp), verticalAlignment = Alignment.Bottom) {
            counts.forEachIndexed { i, count ->
                val isToday = monday.plusDays(i.toLong()) == TODAY
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    if (count > 0) Text("$count", fontSize = 11.sp, color = TextSub)
                    Box(
                        Modifier
                            .width(18.dp)
                            .height(if (count == 0) 3.dp else (60 * count / max).dp)
                            .background(if (isToday) Blue else if (count == 0) Line else BlueLight, RoundedCornerShape(4.dp))
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        labels[i], fontSize = 11.sp,
                        color = if (isToday) Blue else TextSub,
                        fontWeight = if (isToday) FontWeight.Bold else null
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(onProfile: () -> Unit, onTestCrash: () -> Unit) {
    Column(Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)) {
        Text("Cài đặt", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Column(Modifier.background(Color.White, CardShape)) {
            SettingRow(Icons.Outlined.Person, "Hồ sơ", onProfile)
            HorizontalDivider(color = Line)
            SettingRow(Icons.Outlined.BugReport, "Gửi lỗi thử (Crashlytics)", onTestCrash)
        }
    }
}

@Composable
private fun SettingRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = TextSub)
        Spacer(Modifier.width(12.dp))
        Text(label, modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Line)
    }
}
