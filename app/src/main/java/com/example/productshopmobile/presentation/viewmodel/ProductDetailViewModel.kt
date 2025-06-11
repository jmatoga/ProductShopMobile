package com.example.productshopmobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productshopmobile.data.repository.CartRepository
import com.example.productshopmobile.data.repository.ProductRepository
import com.example.productshopmobile.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepo: ProductRepository,
    private val cartRepo: CartRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val _addedToCart = MutableSharedFlow<Boolean>() // służy do emitowania informacji, czy produkt został dodany do koszyka
    val addedToCart: SharedFlow<Boolean> = _addedToCart.asSharedFlow() // do obserwowania przez UI

    private var currentProductId: UUID? = null

    fun loadProduct(id: UUID) {
        currentProductId = id
        viewModelScope.launch {
            productRepo.getProductDetailFlow(id)
                .catch { e ->
                    _uiState.value = ProductDetailUiState.Error("Failed to load the product: ${e.message}")
                }
                .collect {
                    prod -> _uiState.value = ProductDetailUiState.Success(prod)
                }
        }
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            try {
                cartRepo.addToCart(product.id, quantity)
                _addedToCart.emit(true)
                delay(2000)
                _addedToCart.emit(false) // resetuj stan

                currentProductId?.let {
                    loadProduct(it) // ponowne załadowanie produktu
                }
            } catch (e: Exception) {
                _addedToCart.emit(false)
                Log.e("ProductDetailViewModel", "Error during adding product to cart", e)
            }
        }
    }
}