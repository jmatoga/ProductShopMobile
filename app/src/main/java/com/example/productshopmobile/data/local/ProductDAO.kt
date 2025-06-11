package com.example.productshopmobile.data.local

import androidx.room.*
import com.example.productshopmobile.data.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao // Room łączy się z bazą zdefiniowaną w klasie Database
interface ProductDAO {
    @Query("SELECT * FROM products ORDER BY name")
    fun getAll(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getById(id: UUID): Flow<ProductEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun clearAll()
}