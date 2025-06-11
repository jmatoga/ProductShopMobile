package com.example.productshopmobile.presentation.viewmodel

import com.example.productshopmobile.domain.model.Product

sealed class ProductDetailUiState {
    data object Loading : ProductDetailUiState()
    data class Success(val product: Product) : ProductDetailUiState()
    data class Error(val message: String) : ProductDetailUiState()
}