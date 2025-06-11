package com.example.productshopmobile.data.remote.dto

import java.util.UUID

data class CartDTO(
    val id: UUID,
    val userId: UUID,
    val items: List<CartItemDTO> = emptyList()
)