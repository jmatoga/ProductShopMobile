package com.example.productshopmobile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.productshopmobile.data.entity.ProductEntity


@Database(entities = [ProductEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun productDAO(): ProductDAO // interfejs do operacji na bazie dla produktów
}