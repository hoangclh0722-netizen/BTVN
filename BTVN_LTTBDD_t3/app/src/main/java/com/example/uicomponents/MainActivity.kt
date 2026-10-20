package com.example.uicomponents

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import com.example.uicomponents.ui.theme.UIComponentsAppTheme
import coil.compose.AsyncImage


private val Blue = Color(0xFF459BCB)
private val ItemBlue = Color(0xFFB9DDF5)

private data class ComponentItem(val title: String, val description: String)

private val componentGroups = listOf(
    "Display" to listOf(
        ComponentItem("Text", "Displays text"),
        ComponentItem("Image", "Displays an image")
    ),
    "Input" to listOf(
        ComponentItem("TextField", "Input field for text"),
        ComponentItem("PasswordField", "Input field for passwords"),
        ComponentItem("Toggle", "On/off switch"),
        ComponentItem("Slider", "Selects a value from a range"),
        ComponentItem("DatePicker", "Selects a date and time")
    ),
    "Selection" to listOf(
        ComponentItem("Button", "Triggers an action when tapped"),
        ComponentItem("Checkbox", "Selects one or multiple options"),
        ComponentItem("RadioButton", "Selects a single option"),
        ComponentItem("Picker", "Selects a value from a list")
    ),
    "Navigation" to listOf(
        ComponentItem("NavigationView", "Navigation container with title and back button"),
        ComponentItem("TabBar", "Switches between main sections"),
        ComponentItem("Menu", "Shows a menu with multiple actions")
    ),
    "Content" to listOf(
        ComponentItem("List", "Displays a list of items"),
        ComponentItem("Card", "Container with content and style"),
        ComponentItem("Divider", "Separates content into sections")
    ),
    "Layout" to listOf(
        ComponentItem("Column", "Arranges elements vertically"),
        ComponentItem("Row", "Arranges elements horizontally")
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UIComponentsAppTheme(dynamicColor = false) {
                var selectedItem by remember { mutableStateOf<ComponentItem?>(null) }
                Surface(color = Color.White) {
                    if (selectedItem == null) {
                        ComponentList(onSelect = { selectedItem = it })
                    } else {
                        ComponentDetail(item = selectedItem!!, onBack = { selectedItem = null })
                    }
                }
            }
        }
    }
}

@Composable
private fun ComponentList(onSelect: (ComponentItem) -> Unit) {
    Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 16.dp)) {
        Text(
            "UI Components List",
            Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 18.dp),
            color = Blue,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        LazyColumn(
            contentPadding = PaddingValues(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            componentGroups.forEach { (group, components) ->
                item(key = "header-$group") {
                    Text(group, Modifier.fillMaxWidth().padding(bottom = 6.dp), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                }
                items(components, key = { it.title }) { component ->
                    ComponentRow(component, onClick = { onSelect(component) })
                }
            }
        }
    }
}

@Composable
private fun ComponentRow(item: ComponentItem, onClick: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(9.dp)).background(ItemBlue).clickable(onClick = onClick).padding(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(item.title, fontSize = 14.sp, color = Color(0xFF20252B), fontWeight = FontWeight.Bold)
        Text(item.description, fontSize = 13.sp, color = Color(0xFF303840), lineHeight = 17.sp)
    }
}

@Composable
private fun ComponentDetail(item: ComponentItem, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 16.dp)) {
        Box(Modifier.fillMaxWidth().height(52.dp), contentAlignment = Alignment.Center) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Blue)
            }
            Text(item.title, color = Blue, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ComponentExample(item.title)
        }
    }
}

@Composable
private fun ComponentExample(title: String) {
    var text by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    var toggle by remember { mutableStateOf(true) }
    var slider by remember { mutableFloatStateOf(50f) }
    var radio by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf("Select an option") }
    var menuOpen by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf("Select date") }
    var result by remember { mutableStateOf("") }
    val context = androidx.compose.ui.platform.LocalContext.current

    when (title) {
        "Text" -> Text(
            buildAnnotatedString {
                append("The ")
                withStyle(SpanStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)) { append("quick ") }
                withStyle(SpanStyle(color = Color(0xFFB77B2A), fontSize = 25.sp)) { append("Brown\n") }
                append("fox j u m p s ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) { append("over\n") }
                withStyle(SpanStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline)) { append("the ") }
                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("lazy") }
                append(" dog.")
            }, fontSize = 21.sp, lineHeight = 37.sp, color = Color.Black
        )
        "Image" -> Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.uth),
                contentDescription = "Ảnh trong ứng dụng",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            AsyncImage(
                model = "https://th.bing.com/th/id/OIP.S9d81Hbc79S3lYWgEoPhggAAAA?w=269&h=180&c=7&r=0&o=7&dpr=1.3&pid=1.7&rm=3",
                contentDescription = "Ảnh từ Internet",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
        "TextField" -> Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Thông tin nhập") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Text("Tự động cập nhật dữ liệu theo textfield", color = Color(0xFF9F5555), fontSize = 12.sp)
            if (text.isNotEmpty()) Text(text)
        }
        "PasswordField" -> OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        "Toggle" -> Row(verticalAlignment = Alignment.CenterVertically) { Text("On / Off"); Spacer(Modifier.width(16.dp)); Switch(toggle, { toggle = it }) }
        "Slider" -> Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) { Text("Value: ${slider.toInt()}"); Slider(slider, { slider = it }, valueRange = 0f..100f) }
        "DatePicker" -> OutlinedButton(onClick = {
            val today = Calendar.getInstance()
            DatePickerDialog(context, { _, year, month, day -> date = "$day/${month + 1}/$year" }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)).show()
        }) { Text(date) }
        "Button" -> Column(horizontalAlignment = Alignment.CenterHorizontally) { Button(onClick = { result = "Button tapped" }) { Text("Tap me") }; Text(result) }
        "Checkbox" -> Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(checked, { checked = it }); Text("Select this option") }
        "RadioButton" -> Column { listOf("Option 1", "Option 2").forEachIndexed { index, label -> Row(verticalAlignment = Alignment.CenterVertically) { RadioButton(radio == index, { radio = index }); Text(label) } } }
        "Picker" -> Box {
            OutlinedButton(onClick = { menuOpen = true }) { Text(selected) }
            DropdownMenu(menuOpen, { menuOpen = false }) { listOf("Option 1", "Option 2", "Option 3").forEach { option -> DropdownMenuItem(text = { Text(option) }, onClick = { selected = option; menuOpen = false }) } }
        }
        "NavigationView" -> Text("←      ${itemLabel(title)}")
        "TabBar" -> varTabBar()
        "Menu" -> Box {
            OutlinedButton(onClick = { menuOpen = true }) { Text("Open menu") }
            DropdownMenu(menuOpen, { menuOpen = false }) { listOf("Home", "Settings", "About").forEach { DropdownMenuItem(text = { Text(it) }, onClick = { selected = it; menuOpen = false }) } }
        }
        "List" -> Column(verticalArrangement = Arrangement.spacedBy(10.dp)) { listOf("First item", "Second item", "Third item").forEach { Text(it, Modifier.fillMaxWidth().background(ItemBlue, RoundedCornerShape(8.dp)).padding(14.dp)) } }
        "Card" -> Card(colors = CardDefaults.cardColors(containerColor = ItemBlue)) { Text("Content inside a card", Modifier.padding(24.dp)) }
        "Divider" -> Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) { Text("Content above"); HorizontalDivider(); Text("Content below") }
        "Column" -> Column(verticalArrangement = Arrangement.spacedBy(14.dp)) { listOf(Color(0xFFAFC8F2), Color(0xFF5486E8), Color(0xFFAFC8F2)).forEach { Box(Modifier.size(150.dp, 44.dp).background(it, RoundedCornerShape(10.dp))) } }
        "Row" -> Column(verticalArrangement = Arrangement.spacedBy(10.dp)) { repeat(4) { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf(Color(0xFFAFC8F2), Color(0xFF5486E8), Color(0xFFAFC8F2)).forEach { Box(Modifier.weight(1f).height(44.dp).background(it, RoundedCornerShape(10.dp))) } } } }
    }
}

@Composable
private fun varTabBar() {
    var selected by remember { mutableIntStateOf(0) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TabRow(selectedTabIndex = selected) {
            listOf("Home", "Components", "About").forEachIndexed { index, label ->
                Tab(selected == index, onClick = { selected = index }, text = { Text(label, fontSize = 11.sp) })
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(listOf("Home", "Components", "About")[selected])
    }
}

private fun itemLabel(title: String) = if (title == "NavigationView") "NavigationView" else title
