package com.caolehuyhoang.tuan5

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class Product(
    @SerializedName("name") val name: String = "",
    @SerializedName("price") val price: Double = 0.0,
    @SerializedName("imgURL") val image: String = "",
    @SerializedName("des") val description: String = ""
)

interface ProductApi {
    @GET("v2/product")
    suspend fun getProduct(): Product
}

object RetrofitClient {
    val api: ProductApi = Retrofit.Builder()
        .baseUrl("https://mock.apidog.com/m1/890655-872447-default/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProductApi::class.java)
}

sealed interface ProductUiState {
    data object Loading : ProductUiState
    data class Success(val product: Product) : ProductUiState
    data class Error(val message: String) : ProductUiState
}

class ProductViewModel : ViewModel() {
    private val _state = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val state: StateFlow<ProductUiState> = _state

    init {
        loadProduct()
    }

    fun loadProduct() {
        viewModelScope.launch {
            _state.value = ProductUiState.Loading
            _state.value = try {
                ProductUiState.Success(RetrofitClient.api.getProduct())
            } catch (e: Exception) {
                ProductUiState.Error(e.message ?: "Có lỗi xảy ra")
            }
        }
    }
}