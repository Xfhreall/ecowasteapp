package com.example.ecowasteapp.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
    onWasteClick: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    // Filter list
    val filteredList = remember(query, wasteItems) {
        if (query.isBlank()) emptyList() else wasteItems.filter {
            it.name.contains(query, ignoreCase = true)
        }
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
                        .clickable { /* Aksi Buka Kamera */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Scan", tint = Color.White)
                }
            }
        }

        // Konten
        if (query.isBlank()) {
            // Tampilan "Cara Menggunakan" (Sesuai Screenshot)
            InstructionCard()
        } else {
            // Hasil Pencarian
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(filteredList) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clickable { onWasteClick(item.id) },
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(item.name, fontWeight = FontWeight.Bold)
                            Text(item.category, style = MaterialTheme.typography.bodySmall)
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