package com.example.ecowasteapp.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecowasteapp.components.EcoGreen
import com.example.ecowasteapp.components.GreenHeader
import com.example.ecowasteapp.model.WasteItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WasteListScreen(
    wasteItems: List<WasteItem>,
    onWasteClick: (String) -> Unit,
    onCameraClick: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    // Filter list by query and category
    val filteredList = remember(query, selectedCategory, wasteItems) {
        var list = wasteItems
        
        // Filter by category if selected
        if (selectedCategory != null) {
            list = list.filter { it.category.contains(selectedCategory!!, ignoreCase = true) }
        }
        
        // Filter by search query
        if (query.isNotBlank()) {
            list = list.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true)
            }
        }
        
        list
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        // Header
        GreenHeader(
            title = "Cari Sampah",
            subtitle = "Identifikasi jenis sampah dengan foto atau pencarian"
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                // Search Bar
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Cari jenis sampah...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                // Tombol Kamera Hijau
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(EcoGreen, shape = RoundedCornerShape(12.dp))
                        .clickable { onCameraClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Scan", tint = Color.White)
                }
            }
        }

        // Konten
        Column {
            // Category chips (always visible)
            if (selectedCategory != null || query.isNotBlank()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            label = { Text("Semua") }
                        )
                    }
                    items(com.example.ecowasteapp.data.DummyWasteData.categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = if (selectedCategory == category) null else category },
                            label = { Text(category) }
                        )
                    }
                }
            }
            
            // Main content
            if (query.isBlank() && selectedCategory == null) {
                // Tampilan "Cara Menggunakan"
                InstructionCard()
            } else {
                // Hasil Pencarian
                if (filteredList.isEmpty()) {
                    // Empty state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Tidak ada hasil",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Coba kata kunci lain atau pilih kategori berbeda",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(filteredList) { item ->
                            WasteItemCard(
                                item = item,
                                onClick = { onWasteClick(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InstructionCard() {
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)) // Hijau muda sekali
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.CameraAlt,
                contentDescription = null,
                tint = EcoGreen,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Cara Menggunakan", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            InstructionItem("1. Ketik jenis sampah di kolom pencarian, atau")
            InstructionItem("2. Klik tombol kamera dan foto sampah yang ingin diidentifikasi")
            InstructionItem("3. Dapatkan informasi detail tentang cara mengelola sampah tersebut")
        }
    }
}

@Composable
fun InstructionItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = Color.DarkGray,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    )
}

@Composable
fun WasteItemCard(
    item: WasteItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category indicator
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = when {
                            item.category.contains("Organik") -> Color(0xFF4CAF50)
                            item.category.contains("Plastik") -> Color(0xFFFFC107)
                            item.category.contains("Kertas") -> Color(0xFF2196F3)
                            item.category.contains("Kaca") || item.category.contains("Logam") -> Color(0xFF9E9E9E)
                            item.category.contains("B3") -> Color(0xFFF44336)
                            else -> Color.Gray
                        }.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when {
                        item.category.contains("Organik") -> "🌱"
                        item.category.contains("Plastik") -> "♻️"
                        item.category.contains("Kertas") -> "📄"
                        item.category.contains("Kaca") || item.category.contains("Logam") -> "🔩"
                        item.category.contains("B3") -> "⚠️"
                        else -> "🗑️"
                    },
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = EcoGreen
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description.take(80) + "...",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2
                )
            }
        }
    }
}