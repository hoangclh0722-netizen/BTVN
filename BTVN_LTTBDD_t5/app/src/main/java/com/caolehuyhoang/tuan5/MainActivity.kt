package com.caolehuyhoang.tuan5

import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseUser
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.caolehuyhoang.tuan5.ui.theme.BTTheme
import com.caolehuyhoang.tuan5.ui.theme.Background
import com.caolehuyhoang.tuan5.ui.theme.Blue
import com.caolehuyhoang.tuan5.ui.theme.Line
import com.caolehuyhoang.tuan5.ui.theme.TextSub

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
fun App(auth: AuthViewModel = viewModel(), vm: TaskViewModel = viewModel()) {
    val user by auth.user.collectAsState()
    val event by auth.event.collectAsState()
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    val message by vm.message.collectAsState()

    LaunchedEffect(event) {
        when (val e = event) {
            AuthEvent.Success -> Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
            is AuthEvent.Failure -> snackbar.showSnackbar("Đăng nhập thất bại: ${e.message}")
            null -> return@LaunchedEffect
        }
        auth.eventShown()
    }
    LaunchedEffect(message) {
        message?.let {
            snackbar.showSnackbar(it)
            vm.messageShown()
        }
    }
    LaunchedEffect(user?.uid) {
        user?.let { vm.start(it.uid) } ?: vm.stop()
    }

    Scaffold(containerColor = Background, snackbarHost = { SnackbarHost(snackbar) }) { padding ->
        Box(Modifier.padding(bottom = padding.calculateBottomPadding())) {
            val u = user
            if (u == null) LoginScreen(auth)
            else MainScreens(u, vm, onSignOut = auth::signOut)
        }
    }
}

@Composable
private fun MainScreens(user: FirebaseUser, vm: TaskViewModel, onSignOut: () -> Unit) {
    var tab by rememberSaveable { mutableStateOf(Tab.Home) }
    var showProfile by rememberSaveable { mutableStateOf(false) }
    val openId by vm.openId.collectAsState()

    Column {
        Box(Modifier.weight(1f)) {
            val id = openId
            when {
                id != null -> {
                    BackHandler { vm.open(null) }
                    DetailScreen(id, vm, backLabel = tab.label, onBack = { vm.open(null) })
                }
                showProfile -> {
                    BackHandler { showProfile = false }
                    ProfileScreen(user, vm, tab.label, onBack = { showProfile = false }, onSignOut = onSignOut)
                }
                else -> when (tab) {
                    Tab.Home -> HomeScreen(
                        vm, user.displayNameOrGuest(),
                        onProfile = { showProfile = true },
                        onOpen = vm::open,
                        onSeeAll = { tab = Tab.List }
                    )
                    Tab.List -> ListScreen(vm, onOpen = vm::open)
                    Tab.Settings -> SettingsScreen(
                        onProfile = { showProfile = true },
                        onTestCrash = { throw RuntimeException("Lỗi thử Crashlytics") }
                    )
                    Tab.Notify -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("${tab.label} – chưa có trong bài này", color = TextSub)
                    }
                }
            }
        }
        if (openId == null && !showProfile) BottomBar(tab, onTab = { tab = it }, onAdd = {
            vm.showMessage("Thêm công việc sẽ làm ở bài sau")
        })
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
