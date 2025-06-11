package com.example.productshopmobile.presentation.viewmodel

import com.example.productshopmobile.domain.model.Product

sealed class ProductListUiState {
    data object Loading : ProductListUiState()
    data class Success(val products: List<Product>) : ProductListUiState()
    data class Error(val message: String) : ProductListUiState()
}