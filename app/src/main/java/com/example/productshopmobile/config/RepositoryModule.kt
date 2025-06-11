package com.example.productshopmobile.config

import android.content.Context
import com.example.productshopmobile.data.local.ProductDAO
import com.example.productshopmobile.data.local.getOrCreateUserId
import com.example.productshopmobile.data.repository.CartRepository
import com.example.productshopmobile.data.repository.ProductRepository
import com.example.productshopmobile.network.ShopAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.UUID
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideProductRepository(productDAO: ProductDAO, shopAPI: ShopAPI): ProductRepository = ProductRepository(productDAO, shopAPI)

    @Provides
    @Singleton
    fun provideAnonymousUserId(@ApplicationContext context: Context): UUID {
        // RunBlocking bo Hilt potrzebuje synchronicznej wartości w momencie tworzenia grafu zależności
        return runBlocking {
            getOrCreateUserId(context)
        }
    }

    @Provides
    @Singleton
    fun provideCartRepository(shopAPI: ShopAPI, userId: UUID): CartRepository = CartRepository(shopAPI, userId)
}