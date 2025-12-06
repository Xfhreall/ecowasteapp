package com.example.ecowasteapp.page

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onNavigateToArticles: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToTrack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("EcoWaste Menu") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Selamat Datang di EcoWaste",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Menu 1: Kenal Sampah
            MenuButton(text = "Kenal Sampah\n(Artikel Edukasi)", onClick = onNavigateToArticles)

            Spacer(modifier = Modifier.height(16.dp))

            // Menu 2: Cari Sampah
            MenuButton(text = "Cari Sampah\n(Informasi Jenis)", onClick = onNavigateToSearch)

            Spacer(modifier = Modifier.height(16.dp))

            // Menu 3: Sampah Track
            MenuButton(text = "Sampah Track\n(Cari Lokasi)", onClick = onNavigateToTrack)
        }
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
    }
}