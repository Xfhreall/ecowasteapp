package com.example.ecowasteapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecowasteapp.data.DummyWasteData
import com.example.ecowasteapp.model.WasteItem
import android.content.Context
import androidx.navigation.navArgument
import com.example.ecowasteapp.page.WasteDetailScreen
import com.example.ecowasteapp.page.WasteListScreen
import com.example.ecowasteapp.ui.theme.EcowasteappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcowasteappTheme {
                EcoWasteApp()
            }
        }
    }
}


fun openTrashBinInMaps(context: Context) {
    // Mencari "tempat sampah terdekat" menggunakan Google Maps
    val gmmIntentUri = Uri.parse("geo:0,0?q=tempat+sampah+terdekat")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
        setPackage("com.google.android.apps.maps")
    }

    // Jika aplikasi Google Maps ada, gunakan itu, kalau tidak pakai aplikasi lain
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        val fallbackIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        context.startActivity(fallbackIntent)
    }
}

@Composable
fun EcoWasteApp() {
    val navController = rememberNavController()
    val allWasteItems = remember { DummyWasteData.wasteItems }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            WasteListScreen(
                wasteItems = allWasteItems,
                onWasteClick = { wasteId ->
                    navController.navigate("detail/$wasteId")
                }
            )
        }
        composable(
            route = "detail/{wasteId}",
            arguments = listOf(
                navArgument("wasteId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val wasteId = backStackEntry.arguments?.getString("wasteId")
            val item = allWasteItems.find { it.id == wasteId }
            if (item != null) {
                WasteDetailScreen(wasteItem = item)
            } else {
                Text(
                    text = "Data sampah tidak ditemukan.",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    }
}
