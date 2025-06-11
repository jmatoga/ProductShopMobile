package com.example.productshopmobile.util

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.productshopmobile.data.repository.ProductRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.TimeUnit

// @AssistedInject bo część zależności musi być przekazywana przez WorkManagera, a część przez config
@HiltWorker
class SyncProductsWorker @AssistedInject constructor(
    // context i params są przekazywane przez WorkManagera więc trzeba dać @Assisted
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val productRepository: ProductRepository
) : CoroutineWorker(context, params) {
    // dziedziczenie po CoroutineWorker umożliwia korzystanie z asynchronicznych, lekkich wątków (kotuyn)
    // korutyny muszą się zakończyć przed końcem pracy workera
    override suspend fun doWork(): Result = coroutineScope {
        try {
            productRepository.getProductsFlow().collect {} // uruchamia Flow i czeka na jego zakończenie
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncProductsWorker", "Flow error", e)
            Result.retry()
        }
    }

    companion object {
        // cykliczne uruchamianie workera wraz z odpowiednimi warunkami
        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // warunek
                .build()

            val request = PeriodicWorkRequestBuilder<SyncProductsWorker>(1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

            // dodaje cykliczne zadanie, które nie będzie powielane, jeśli już istnieje
            WorkManager.Companion.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
                "sync_products", ExistingPeriodicWorkPolicy.KEEP, request)
        }
    }
}