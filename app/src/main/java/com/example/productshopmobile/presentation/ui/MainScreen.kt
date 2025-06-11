package com.example.productshopmobile.presentation.ui

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.productshopmobile.data.local.getOrCreateUserId
import java.util.UUID

@Composable
fun MainScreen() {
    val context = LocalContext.current // dostęp do Androidowego kontekstu (potrzebny do DataStore)
    var userId by remember { mutableStateOf<UUID?>(null) }

    // uruchamia się tylko raz przy uruchomieniu composable
    LaunchedEffect(Unit) {
        userId = getOrCreateUserId(context)
    }

    if (userId != null) {
        val navController = rememberNavController() // zarządza aktualnym ekranem i przechodzeniem między nimi
        NavHost(navController, startDestination = "product_list") {
            composable("product_list") { // definicja trasy
                ProductListScreen(onProductClick = { productId ->
                    navController.navigate("product_detail/${userId}/${productId}")
                }, onCartClick = {
                    navController.navigate("cart/${userId}")
                })
            }
            composable("product_detail/{userId}/{productId}") { backStackEntry ->
                val userId = UUID.fromString(backStackEntry.arguments?.getString("userId") ?: "")
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                ProductDetailScreen(
                    productId = UUID.fromString(productId), onCartClick = {
                        navController.navigate("cart/${userId}")
                    })
            }
            composable("cart/{userId}") { backStackEntry ->
                val userId = UUID.fromString(backStackEntry.arguments?.getString("userId") ?: "")
                CartScreen(userId = userId)
            }
        }
    } else {
        CircularProgressIndicator() // ekran ładowania
    }
}