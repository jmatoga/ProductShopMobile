package com.example.productshopmobile.network
import com.example.productshopmobile.data.remote.dto.CartDTO
import com.example.productshopmobile.data.remote.dto.ProductDTO
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface ShopAPI {
    @GET("/api/v1/products")
    suspend fun getProducts(): List<ProductDTO>

    @GET("/api/v1/products/{id}")
    suspend fun getProductDetail(@Path("id") id: UUID): ProductDTO

    // Tworzenie lub odświeżenie koszyka
    @POST("/api/v1/user/{userId}/cart")
    suspend fun createCart(@Path("userId") userId: UUID)

    @POST("/api/v1/user/{userId}/cart/add-product")
    suspend fun addToCart(@Path("userId") userId: UUID, @Query("productId") productId: UUID, @Query("quantity") quantity: Int)

    @DELETE("/api/v1/user/{userId}/cart/remove-product")
    suspend fun removeFromCart(@Path("userId") userId: UUID, @Query("productId") productId: UUID, @Query("quantity") quantity: Int)

    @POST("/api/v1/user/{userId}/cart/checkout")
    suspend fun checkoutCart(@Path("userId") userId: UUID)

    // Pobranie zawartości koszyka
    @GET("/api/v1/user/{userId}/cart")
    suspend fun getCartItems(@Path("userId") userId: UUID): CartDTO

    @GET("/api/v1/user/{userId}/cart/{cartId}")
    suspend fun getCartById(@Path("userId") userId: UUID, @Path("cartId") cartId: UUID): CartDTO

    // Pobranie sumy wartości koszyka
    @GET("/api/v1/user/{userId}/cart/total")
    suspend fun getCartTotal(@Path("userId") userId: UUID): Double
}