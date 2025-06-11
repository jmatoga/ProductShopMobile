package com.example.productshopmobile.data.local

import androidx.room.TypeConverter
import java.util.UUID

// domyślnie Room obsługuje tylko konwersję typów prostych
class Converters {
    @TypeConverter
    fun uuidToString(value: UUID?): String? = value?.toString()

    @TypeConverter
    fun stringToUUID(value: String?): UUID? = value?.let { UUID.fromString(it) }
}