package com.example.productshopmobile.data.repository

import com.example.productshopmobile.data.remote.dto.CartDTO
import com.example.productshopmobile.data.remote.dto.toDomain
import com.example.productshopmobile.domain.model.CartItem
import com.example.productshopmobile.network.ShopAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val api: ShopAPI,
    private val userId: UUID
) {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())

    // Zwracany jest tylko StateFlow (czyli niemodyfikowalny widok Flow),
    // komponenty zewnętrzne mogą obserwować zmiany koszyka, ale nie mogą go zmieniać
    fun getCartItems(): Flow<List<CartItem>> = _cartItems.asStateFlow()

    private suspend fun fetchAndPublishCurrentCart() {
        withContext(Dispatchers.IO) { // przenosi pracę do wątku I/O czyli operacje sieciowe nie blokują głównego wątku
            try {
                val cartDto = api.getCartItems(userId)
                // konwertuje DTO na model domenowy CartItem przed zapisaniem do Flow
                _cartItems.value = cartDto.items.map { it.toDomain() }
                // Aktualizuje StateFlow dzięki czemu UI automatycznie zareaguje na zmiany
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun addToCart(productId: UUID, quantity: Int) {
        api.addToCart(userId, productId, quantity)
        fetchAndPublishCurrentCart() // aktualizuje lokalny StateFlow, pobierając dane z serwera
    }

    suspend fun removeFromCart(productId: UUID, quantity: Int) {
        api.removeFromCart(userId, productId, quantity)
        fetchAndPublishCurrentCart()
    }

    suspend fun createCart() {
        api.createCart(userId)
    }

    suspend fun checkoutCart() {
        api.checkoutCart(userId)
        fetchAndPublishCurrentCart()
    }

    fun getCartById(cartId: UUID): Flow<CartDTO> = flow {
        val cartDto = api.getCartById(userId, cartId)
        emit(cartDto) // wysyła wartość total do obserwatorów Flow czyli do ViewModelu
    }.flowOn(Dispatchers.IO) // zapewnia, że operacja będzie wykonana na dispatcherze I/O

    fun getCartTotal(): Flow<Double> = flow {
        val total = api.getCartTotal(userId)
        emit(total)
    }.flowOn(Dispatchers.IO)
}