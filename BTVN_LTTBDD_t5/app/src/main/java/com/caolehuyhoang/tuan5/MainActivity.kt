package com.caolehuyhoang.tuan5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.caolehuyhoang.tuan5.ui.theme.BTTheme

private val Blue = Color(0xFF2F80ED)
private val LightBlue = Color(0xFFE3EEFD)
private val PriceRed = Color(0xFFD32F2F)
private val GrayBox = Color(0xFFF2F2F2)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BTTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProductDetailScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        TopBar()

        when (val s = state) {
            is ProductUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            is ProductUiState.Error -> Box(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Lỗi: ${s.message}", textAlign = TextAlign.Center)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Thử lại",
                        color = Blue,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { viewModel.loadProduct() }
                    )
                }
            }

            is ProductUiState.Success -> ProductContent(s.product)
        }
    }
}

@Composable
private fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Nút back tròn nền xanh nhạt
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Blue)
                .align(Alignment.CenterStart)
                .clickable { /* TODO: quay lại */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        // Tiêu đề căn giữa
        Text(
            text = "Product detail",
            color = Blue,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ProductContent(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Ảnh sản phẩm bo góc
        AsyncImage(
            model = product.image,
            contentDescription = product.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(LightBlue)
        )

        // Tên sản phẩm
        Text(
            text = product.name,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        // Giá màu đỏ: 4000000 -> "Giá: 4.000.000₫"
        Text(
            text = "Giá: ${formatPrice(product.price)}",
            color = PriceRed,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        // Mô tả trong khung xám bo góc
        Text(
            text = product.description,
            fontSize = 13.sp,
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(GrayBox)
                .padding(12.dp)
        )

        Spacer(Modifier.height(16.dp))
    }
}

private fun formatPrice(price: Double): String =
    String.format("%,d", price.toLong()).replace(',', '.') + "₫"