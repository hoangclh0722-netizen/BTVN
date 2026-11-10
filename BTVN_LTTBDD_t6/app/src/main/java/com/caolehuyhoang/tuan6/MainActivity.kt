package com.caolehuyhoang.tuan6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.caolehuyhoang.tuan6.ui.theme.BTTheme
import com.caolehuyhoang.tuan6.ui.theme.Background
import com.caolehuyhoang.tuan6.ui.theme.Blue
import com.caolehuyhoang.tuan6.ui.theme.Line
import com.caolehuyhoang.tuan6.ui.theme.TextSub

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BTTheme { App() }
        }
    }
}

enum class Tab(val label: String, val icon: ImageVector) {
    Home("Trang chủ", Icons.Outlined.Home),
    List("Danh sách", Icons.AutoMirrored.Outlined.List),
    Notify("Thông báo", Icons.Outlined.Notifications),
    Settings("Cài đặt", Icons.Outlined.Tune)
}

@Composable
fun App(vm: TaskViewModel = viewModel()) {
    var tab by rememberSaveable { mutableStateOf(Tab.Home) }
    var openId by rememberSaveable { mutableStateOf<Int?>(null) }
    val snackbar = remember { SnackbarHostState() }
    val message by vm.message.collectAsState()

    LaunchedEffect(message) {
        message?.let {
            snackbar.showSnackbar(it)
            vm.messageShown()
        }
    }

    val open: (Int) -> Unit = { id ->
        vm.openTask(id)
        openId = id
    }

    Scaffold(
        containerColor = Background,
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            if (openId == null) BottomBar(tab, onTab = { tab = it }, onAdd = {
                vm.showMessage("Thêm công việc sẽ làm ở bài sau")
            })
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            val id = openId
            if (id != null) {
                BackHandler { openId = null }
                DetailScreen(id, vm, backLabel = tab.label, onBack = { openId = null })
            } else when (tab) {
                Tab.Home -> HomeScreen(vm, onOpen = open, onSeeAll = { tab = Tab.List })
                Tab.List -> ListScreen(vm, onOpen = open)
                else -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("${tab.label} – chưa có trong bài này", color = TextSub)
                }
            }
        }
    }
}

@Composable
private fun BottomBar(current: Tab, onTab: (Tab) -> Unit, onAdd: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
    ) {
        Box(Modifier.fillMaxWidth().size(1.dp).background(Line))
        Row(
            Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BarItem(Tab.Home, current, onTab)
            BarItem(Tab.List, current, onTab)
            Box(
                Modifier
                    .offset(y = (-12).dp)
                    .size(52.dp)
                    .background(Blue, CircleShape)
                    .clickable(onClick = onAdd),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Add, "Thêm", tint = Color.White)
            }
            BarItem(Tab.Notify, current, onTab)
            BarItem(Tab.Settings, current, onTab)
        }
    }
}

@Composable
private fun BarItem(tab: Tab, current: Tab, onTab: (Tab) -> Unit) {
    val color = if (tab == current) Blue else TextSub
    Column(
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) { onTab(tab) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(tab.icon, tab.label, tint = color, modifier = Modifier.size(22.dp))
        Text(tab.label, color = color, fontSize = 11.sp)
    }
}
