package com.example.productshopmobile.domain.model

import java.util.*

data class Product(
    val id: UUID,
    val name: String,
    val description: String?,
    val price: Double,
    val availableQty: Int
)