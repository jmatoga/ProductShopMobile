package com.example.productshopmobile.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.productshopmobile.presentation.viewmodel.CartUiState
import com.example.productshopmobile.presentation.viewmodel.CartViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    userId: UUID, viewModel: CartViewModel = hiltViewModel() // ViewModel wstrzykiwany automatycznie przez Hilt
) {
    val state by viewModel.uiState.collectAsState() // Flow reprezentujący stan UI (np. loading, sukces, błąd)
    // collectAsState() konwertuje Flow na reaktywną zmienną Compose
    LaunchedEffect(Unit) { viewModel.fetchCart() } // automatyczne załadowanie koszyka tylko raz przy wejściu na ekran
    // tworzy standardowy szkielet UI: AppBar u góry, przestrzeń na treść poniżej
    Scaffold(topBar = { TopAppBar(title = { Text("Cart") }) }) { padding ->
        // Box pozwala nakładać elementy na siebie (np. loader na środku), marginesy przekazane przez Scaffold, fillMaxSize() — wypełnia całą dostępną przestrzeń
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (state) { // w zależności od stanu
                CartUiState.Loading -> {
                    // Pokazuje spinner ładowania na środku ekranu
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is CartUiState.Success -> {
                    // pobranie listy produktów w koszyku i suma
                    val items = (state as CartUiState.Success).items
                    val total = (state as CartUiState.Success).total
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (items.isEmpty()) {
                            // Wyświetla komunikat na środku
                            Text(text = "Your cart is empty", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                        } else {
                            // lista przewijalna
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(items) { item ->
                                    // pojedynczy produkt w koszyku
                                    CartListItem(item = item, onRemove = {
                                            // Po kliknięciu w ikonę kosza, wywołuje ViewModel, by usunąć daną ilość produktu z koszyka
                                            viewModel.removeFromCart(productId = item.productId, quantity = item.quantity)
                                        }
                                    )
                                    HorizontalDivider() // linia oddzielająca elementy
                                }
                            }
                            // Wyświetla łączną cenę koszyka, wyrównaną do prawej
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Total: $total PLN", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.align(Alignment.End))
                        }
                    }
                }

                // Pokazuje komunikat błędu na środku
                is CartUiState.Error -> {
                    Text(text = (state as CartUiState.Error).message, modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}