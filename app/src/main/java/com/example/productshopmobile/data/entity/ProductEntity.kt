package com.example.productshopmobile.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.productshopmobile.data.remote.dto.ProductDTO
import com.example.productshopmobile.domain.model.Product
import java.util.UUID

// Tabela w lokalnej Room
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: UUID,
    val name: String,
    val description: String?,
    val price: Double,
    val availableQty: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    // konwersja z encji bazy na model domenowy
    fun toDomain(): Product = Product(id, name, description, price, availableQty)

    companion object {
        fun fromDto(dto: ProductDTO): ProductEntity = ProductEntity(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            price = dto.price,
            availableQty = dto.availableQty
        )
    }
}