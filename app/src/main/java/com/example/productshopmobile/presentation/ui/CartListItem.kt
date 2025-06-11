package com.example.productshopmobile.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.productshopmobile.domain.model.CartItem

@Composable
fun CartListItem(item: CartItem, onRemove: () -> Unit // lambda do wykonania po kliknięciu "Usuń"
) {
    // zajmuje całą szerokość, dodaje padding, elementy w pionie są wyśrodkowane
    // Row zapewnia poziomy układ elementów
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        // kolumna zajmuje całą dostępną przestrzeń w Row, a przycisk „Usuń” tylko tyle, ile potrzebuje
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.productName, style = MaterialTheme.typography.titleLarge)
            Text(text = "Quantity: ${item.quantity}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Price: ${item.price} PLN", style = MaterialTheme.typography.bodyLarge)
        }
        IconButton(onClick = onRemove) {
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Remove")
        }
    }
}