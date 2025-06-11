package com.example.productshopmobile

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import androidx.hilt.work.HiltWorkerFactory
import javax.inject.Inject

// Hilt ma przygotować config dla całej aplikacji
// Application() - dziedzicząc startuje razem z aplikacją
@HiltAndroidApp
class ProductShopMobileApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory // fabryka, która pozwala na tworzenie zadań (Worker) z wstrzykniętymi zależnościami

    override fun onCreate() {
        super.onCreate()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}