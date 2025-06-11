package com.example.productshopmobile.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.productshopmobile.presentation.viewmodel.ProductDetailUiState
import com.example.productshopmobile.presentation.viewmodel.ProductDetailViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: UUID,
    onCartClick: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val addedToCart by viewModel.addedToCart.collectAsState(initial = false)

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId) // do pobrania szczegółów produktu z API/bazy
    }

    // podstawowy layout, który ma pasek nawigacyjny
    Scaffold(topBar = {
            TopAppBar(title = { Text("Product Details", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = onCartClick) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (state) {
                ProductDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is ProductDetailUiState.Success -> {
                    val product = (state as ProductDetailUiState.Success).product

                    Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
                        Text(text = product.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(text = product.description ?: "No description available", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(text = "Price: ${product.price} PLN", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Available Qunatity: ${product.availableQty}", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(onClick = { viewModel.addToCart(product, 1) }, enabled = product.availableQty > 0) {
                            Text("Add to Cart")
                        }

                        if (addedToCart) {
                            Text(text = "Added to cart!", color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 12.dp), style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                is ProductDetailUiState.Error -> {
                    Text(text = (state as ProductDetailUiState.Error).message, modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}