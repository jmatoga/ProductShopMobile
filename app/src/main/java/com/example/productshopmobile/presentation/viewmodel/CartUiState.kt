package com.example.productshopmobile.presentation.viewmodel

import com.example.productshopmobile.domain.model.CartItem

sealed class CartUiState {
    data object Loading : CartUiState()
    data class Success(val items: List<CartItem>, val total: Double) : CartUiState()
    data class Error(val message: String) : CartUiState()
}