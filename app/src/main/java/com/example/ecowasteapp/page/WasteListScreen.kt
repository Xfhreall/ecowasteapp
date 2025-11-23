package com.example.ecowasteapp.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecowasteapp.model.WasteItem

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun WasteListScreen(
    wasteItems: List<WasteItem>,
    onWasteClick: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    val filteredList = remember(query, wasteItems) {
        if (query.isBlank()) wasteItems
        else wasteItems.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EcoWaste – Edukasi Sampah") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { newValue ->
                    query = newValue
                },
                label = { Text("Cari jenis sampah (misal: botol, kertas)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tidak ada data sampah dengan kata kunci tersebut.")
                }
            } else {
                LazyColumn {
                    items(filteredList) { item ->
                        WasteListItem(
                            wasteItem = item,
                            onClick = { onWasteClick(item.id) }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun WasteListItem(
    wasteItem: WasteItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = wasteItem.name,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = wasteItem.category,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = wasteItem.description,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2
        )
    }
}
