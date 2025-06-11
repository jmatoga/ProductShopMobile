package com.example.productshopmobile.data.remote.dto

import com.example.productshopmobile.domain.model.Product
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class ProductDTO(
    val id: UUID,
    val name: String,
    val description: String?,
    val price: Double,
    @Json(name = "availableQuantity")
    val availableQty: Int,
    ) {
    fun toDomain(): Product = Product(id, name, description, price, availableQty)
}