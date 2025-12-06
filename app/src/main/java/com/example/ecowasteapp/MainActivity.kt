package com.example.ecowasteapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ecowasteapp.data.DummyWasteData
import com.example.ecowasteapp.page.*
import com.example.ecowasteapp.ui.theme.EcowasteappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcowasteappTheme {
                // STATE LOGIN (DUMMY)
                // Default false = Belum Login.
                var isLoggedIn by remember { mutableStateOf(false) }

                if (isLoggedIn) {
                    // Jika SUDAH Login, tampilkan Menu Utama (MainScreen)
                    MainScreen(onLogout = {
                        isLoggedIn = false // Aksi Logout
                    })
                } else {
                    // Jika BELUM Login, tampilkan Halaman Auth/Login
                    AuthScreen(onLoginSuccess = {
                        isLoggedIn = true // Aksi Login Berhasil
                    })
                }
            }
        }
    }
}

// Data class untuk Item Navigasi Bawah
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector, val selectedIcon: ImageVector) {
    object Kenal : BottomNavItem("articles", "Kenal", Icons.Outlined.Article, Icons.Filled.Article)
    object Cari : BottomNavItem("search", "Cari", Icons.Outlined.CameraAlt, Icons.Filled.CameraAlt)
    object Community : BottomNavItem("community", "Komunitas", Icons.Outlined.Groups, Icons.Filled.Groups) // Tab Baru
    object Track : BottomNavItem("track", "Track", Icons.Outlined.LocationOn, Icons.Filled.LocationOn)
}

@Composable
fun MainScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()

    // MENAMBAHKAN TAB "KOMUNITAS" KE LIST
    val items = listOf(
        BottomNavItem.Kenal,
        BottomNavItem.Cari,
        BottomNavItem.Community, // Tab baru ditambahkan di sini
        BottomNavItem.Track
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // Sembunyikan BottomBar jika sedang di halaman Detail
            if (currentRoute?.startsWith("detail") == false) {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (currentRoute == item.route) item.selectedIcon else item.icon,
                                    contentDescription = item.title,
                                    tint = if (currentRoute == item.route) Color(0xFF2E7D32) else Color.Gray
                                )
                            },
                            label = {
                                Text(
                                    item.title,
                                    color = if (currentRoute == item.route) Color(0xFF2E7D32) else Color.Gray,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Kenal.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Route KENAL SAMPAH
            composable(BottomNavItem.Kenal.route) { ArticleListScreen() }

            // 2. Route CARI SAMPAH
            composable(BottomNavItem.Cari.route) {
                WasteListScreen(
                    wasteItems = DummyWasteData.wasteItems,
                    onWasteClick = { id -> navController.navigate("detail/$id") }
                )
            }

            // 3. Route KOMUNITAS (BARU)
            composable(BottomNavItem.Community.route) { CommunityScreen() }

            // 4. Route TRACK
            composable(BottomNavItem.Track.route) { WasteTrackScreen() }
    
            // Route DETAIL
            composable(
                route = "detail/{wasteId}",
                arguments = listOf(navArgument("wasteId") { type = NavType.StringType })
            ) { backStackEntry ->
                val wasteId = backStackEntry.arguments?.getString("wasteId")
                val item = DummyWasteData.wasteItems.find { it.id == wasteId }
                if (item != null) {
                    WasteDetailScreen(wasteItem = item)
                }
            }
        }
    }
}

// Fungsi helper maps (tetap sama)
fun openTrashBinInMaps(context: Context) {
    val gmmIntentUri = Uri.parse("geo:0,0?q=tempat+sampah+terdekat")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
        setPackage("com.google.android.apps.maps")
    }
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        context.startActivity(Intent(Intent.ACTION_VIEW, gmmIntentUri))
    }
}