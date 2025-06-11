package com.example.productshopmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.productshopmobile.presentation.theme.ProductShopMobileTheme
import com.example.productshopmobile.presentation.ui.MainScreen
import com.example.productshopmobile.util.SyncProductsWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SyncProductsWorker.schedule(applicationContext) // Dzięki temu produkty będą synchronizowane automatycznie w tle, niezależnie od UI
        setContent {
            ProductShopMobileTheme {
                MainScreen()
            }
        }
    }
}