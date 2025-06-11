package com.example.productshopmobile.data.remote.dto

import com.example.productshopmobile.domain.model.CartItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class CartItemDTO(
    val id: UUID,
    val cartId: UUID,
    val productId: UUID,
    @Json(name = "name")
    val productName: String,
    val quantity: Int,
    val price: Double
)

// Funkcja rozszerzająca do mapowania DTO na model domenowy
fun CartItemDTO.toDomain(): CartItem {
    return CartItem(
        id = this.id,
        cartId = this.cartId,
        productId = this.productId,
        productName = this.productName,
        quantity = this.quantity,
        price = this.price
    )
}