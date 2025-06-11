package com.example.productshopmobile.config

import android.content.Context
import androidx.room.Room
import com.example.productshopmobile.data.local.Database
import com.example.productshopmobile.data.local.ProductDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): Database {
        return Room.databaseBuilder(appContext, Database::class.java, "shop-db").build() // tworzy instancję bazy danych Room
    }

    @Provides
    fun provideProductDao(db: Database): ProductDAO = db.productDAO()
}