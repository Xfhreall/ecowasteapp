package com.example.ecowasteapp.page

import androidx.compose.foundation.layout.Arrangement // <--- PENTING: Pengganti MainAxisAlignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecowasteapp.components.EcoGreen
import com.example.ecowasteapp.components.GreenHeader
import com.example.ecowasteapp.openTrashBinInMaps

@Composable
fun WasteTrackScreen() {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        // Header (Tanpa Search Bar di dalam header, karena desain map beda dikit)
        GreenHeader(
            title = "Sampah Track",
            subtitle = "Temukan lokasi tempat pembuangan sampah terdekat"
        ) {
            // Bisa dikosongkan jika tidak ada widget spesifik di header
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Konten Map & List
        Column(modifier = Modifier.padding(16.dp)) {
            // Card Peta Placeholder
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().height(200.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = EcoGreen, modifier = Modifier.size(40.dp))
                        Text("Map akan dimuat di sini", color = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { openTrashBinInMaps(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = EcoGreen)
                        ) {
                            Text("Buka Google Maps")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // List Lokasi Terdekat
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Lokasi Terdekat", fontWeight = FontWeight.Bold)
                Surface(color = Color.LightGray.copy(alpha=0.5f), shape = RoundedCornerShape(50)) {
                    Text("3 lokasi", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Item Lokasi Dummy
            LocationItem(name = "TPS Jl. Sudirman", address = "Jl. Sudirman No. 123", type = "TPS Umum")
            Spacer(modifier = Modifier.height(8.dp))
            LocationItem(name = "Bank Sampah Melati", address = "Jl. Mawar Indah No. 4", type = "Bank Sampah")
        }
    }
}


@Composable
fun LocationItem(name: String, address: String, type: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // PERBAIKAN DI SINI: Menggunakan horizontalArrangement, BUKAN MainAxisAlignment
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // <--- INI YANG BENAR
            ) {
                Text(name, fontWeight = FontWeight.Bold)
            }

            Surface(
                color = EcoGreen.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = type,
                    color = EcoGreen,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(4.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn, // Gunakan Icons.Default
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(address, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = EcoGreen)
            ) {
                Text("Arahkan")
            }
        }
    }
}