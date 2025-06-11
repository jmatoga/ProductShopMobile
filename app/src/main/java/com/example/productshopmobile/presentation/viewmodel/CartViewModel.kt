package com.example.productshopmobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productshopmobile.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        fetchCart()
    }

    fun fetchCart() {
        // automatyczne zarządzanie cyklem życia tzw korutyna
        viewModelScope.launch {
            cartRepository.getCartItems()
                .catch { e ->
                    _uiState.value = CartUiState.Error("Failed to load or update cart")
                    Log.e("CartViewModel", "Error during removing product from cart", e)
                }
                .collect { items ->
                    val total = items.sumOf { it.price * it.quantity }
                    _uiState.value = CartUiState.Success(items, total)
                }
        }
    }

    fun removeFromCart(productId: UUID, quantity: Int) {
        viewModelScope.launch {
            val currentItems = (_uiState.value as? CartUiState.Success)?.items?.toMutableList()
            val productIndex = currentItems?.indexOfFirst { it.productId == productId }
            if (productIndex != null && productIndex >= 0) {
                val item = currentItems[productIndex]
                val newQuantity = item.quantity - quantity
                if (newQuantity <= 0) {
                    currentItems.removeAt(productIndex)
                } else {
                    currentItems[productIndex] = item.copy(quantity = newQuantity)
                }
                _uiState.value = CartUiState.Success(currentItems, currentItems.sumOf { it.price * it.quantity })
            }
            try {
                cartRepository.removeFromCart(productId = productId, quantity = quantity)
                fetchCart()
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error("Error during remove product from cart")
                Log.e("CartViewModel", "Error during removing product from cart", e)
            }
        }
    }
}