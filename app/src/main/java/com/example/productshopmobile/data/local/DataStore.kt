package com.example.productshopmobile.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.util.UUID

// umożliwienie dostępu do DataStore z dowolnego miejsca
val Context.userIdDataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

suspend fun getOrCreateUserId(context: Context): UUID {
    val key = stringPreferencesKey("user_id") // klucz do odczytu/zapisu wartości w DataStore
    val preferences = context.userIdDataStore.data.first()
    val currentId = preferences[key]

    // jeśli istnieje id zwracamy, jeśli nie tworzymy nowy i zapisujemy do DataStore
    return if (currentId != null) {
        UUID.fromString(currentId)
    } else {
        val newId = UUID.randomUUID()
        context.userIdDataStore.edit {
            it[key] = newId.toString()
        }
        newId
    }
}