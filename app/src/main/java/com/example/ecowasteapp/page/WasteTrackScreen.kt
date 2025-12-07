package com.example.ecowasteapp.page

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecowasteapp.components.EcoGreen
import com.example.ecowasteapp.components.GreenHeader
import com.example.ecowasteapp.utils.LocationHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

data class WasteLocation(
    val name: String,
    val address: String,
    val type: String,
    val latLng: LatLng
)

@Composable
fun WasteTrackScreen() {
    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }
    val scope = rememberCoroutineScope()
    
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var hasLocationPermission by remember { mutableStateOf(locationHelper.hasLocationPermission()) }
    var isLoadingLocation by remember { mutableStateOf(false) }
    var shouldRequestPermission by remember { mutableStateOf(false) }
    var locationAddress by remember { mutableStateOf("Memuat lokasi...") }
    
    val defaultLocation = LatLng(-6.2088, 106.8456)
    val displayLocation = currentLocation ?: defaultLocation
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(displayLocation, 15f)
    }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions.values.any { it }
        if (hasLocationPermission) {
            isLoadingLocation = true
            locationAddress = "Mencari lokasi Anda..."
            
            scope.launch {
                try {
                    kotlinx.coroutines.withTimeout(15000L) {
                        val location = locationHelper.getCurrentLocation()
                        location?.let {
                            android.util.Log.d("WasteTrack", "Location found: ${it.latitude}, ${it.longitude}, accuracy: ${it.accuracy}m")
                            currentLocation = LatLng(it.latitude, it.longitude)
                            
                            locationAddress = "Mengambil nama lokasi..."
                            val address = locationHelper.getAddressFromLocation(it.latitude, it.longitude)
                            locationAddress = address
                            
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newLatLngZoom(
                                    LatLng(it.latitude, it.longitude),
                                    16f
                                ),
                                durationMs = 1000
                            )
                            
                            android.widget.Toast.makeText(
                                context,
                                "Lokasi ditemukan!",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        } ?: run {
                            android.util.Log.e("WasteTrack", "Location is null")
                            locationAddress = "GPS tidak dapat menemukan lokasi"
                            android.widget.Toast.makeText(
                                context,
                                "Tidak dapat menemukan lokasi. Pastikan GPS aktif dan coba di luar ruangan.",
                                android.widget.Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: kotlinx.coroutines.TimeoutCancellationException) {
                    android.util.Log.e("WasteTrack", "Timeout getting location")
                    locationAddress = "Menggunakan lokasi default (Jakarta)"
                    android.widget.Toast.makeText(
                        context,
                        "GPS timeout. Coba aktifkan mode akurasi tinggi di Settings.",
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                }
                isLoadingLocation = false
            }
        } else {
            locationAddress = "Izin lokasi ditolak"
            android.widget.Toast.makeText(
                context,
                "Izin lokasi diperlukan untuk menggunakan fitur ini.",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    LaunchedEffect(Unit) {
        if (hasLocationPermission) {
            isLoadingLocation = true
            locationAddress = "Mencari lokasi Anda..."
            
            try {
                kotlinx.coroutines.withTimeout(15000L) {
                    val location = locationHelper.getCurrentLocation()
                    location?.let {
                        android.util.Log.d("WasteTrack", "Location found: ${it.latitude}, ${it.longitude}, accuracy: ${it.accuracy}m")
                        currentLocation = LatLng(it.latitude, it.longitude)
                        
                        // Get address from coordinates
                        locationAddress = "Mengambil nama lokasi..."
                        val address = locationHelper.getAddressFromLocation(it.latitude, it.longitude)
                        locationAddress = address
                        
                        android.util.Log.d("WasteTrack", "Address: $address")
                        
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLngZoom(
                                LatLng(it.latitude, it.longitude),
                                16f
                            ),
                            durationMs = 1000
                        )
                        
                        android.widget.Toast.makeText(
                            context,
                            "Lokasi ditemukan!",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    } ?: run {
                        android.util.Log.e("WasteTrack", "Location is null")
                        locationAddress = "GPS tidak dapat menemukan lokasi"
                        android.widget.Toast.makeText(
                            context,
                            "Tidak dapat menemukan lokasi. Pastikan GPS aktif, izinkan akses lokasi, dan coba di luar ruangan.",
                            android.widget.Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: kotlinx.coroutines.TimeoutCancellationException) {
                android.util.Log.e("WasteTrack", "Timeout getting location")
                locationAddress = "Menggunakan lokasi default (Jakarta)"
                android.widget.Toast.makeText(
                    context,
                    "GPS timeout. Tips: Aktifkan 'Mode Akurasi Tinggi' di Settings → Lokasi, dan coba di outdoor.",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                android.util.Log.e("WasteTrack", "Error getting location: ${e.message}")
                locationAddress = "Error mendapatkan lokasi"
                android.widget.Toast.makeText(
                    context,
                    "Error: ${e.message}",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
            
            isLoadingLocation = false
        } else {
            // Request permission on first launch
            locationAddress = "Izin lokasi diperlukan"
            shouldRequestPermission = true
        }
    }
    
    // Trigger permission request
    LaunchedEffect(shouldRequestPermission) {
        if (shouldRequestPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            shouldRequestPermission = false
        }
    }
    
    // Dummy waste locations (relatif ke lokasi user atau default)
    val wasteLocations = remember(displayLocation) {
        listOf(
            WasteLocation(
                "TPS Jl. Sudirman",
                "Jl. Sudirman No. 123",
                "TPS Umum",
                LatLng(displayLocation.latitude + 0.005, displayLocation.longitude + 0.003)
            ),
            WasteLocation(
                "Bank Sampah Melati",
                "Jl. Mawar Indah No. 4",
                "Bank Sampah",
                LatLng(displayLocation.latitude - 0.003, displayLocation.longitude + 0.005)
            ),
            WasteLocation(
                "TPA Regional",
                "Jl. Pembuangan Raya",
                "TPA",
                LatLng(displayLocation.latitude + 0.008, displayLocation.longitude - 0.002)
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        // Header
        GreenHeader(
            title = "Sampah Track",
            subtitle = "Temukan lokasi tempat pembuangan sampah terdekat"
        ) {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Konten Map & List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Location Info Card
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = EcoGreen.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = null,
                            tint = EcoGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Lokasi Anda",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = EcoGreen
                            )
                            Text(
                                text = locationAddress,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.DarkGray
                            )
                        }
                        if (isLoadingLocation) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = EcoGreen,
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }
            
            item {
                // Google Maps
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            properties = MapProperties(
                                isMyLocationEnabled = hasLocationPermission
                            ),
                            uiSettings = MapUiSettings(
                                zoomControlsEnabled = false,
                                myLocationButtonEnabled = false,
                                compassEnabled = true,
                                mapToolbarEnabled = false
                            )
                        ) {
                            // User location marker (custom marker)
                            currentLocation?.let {
                                Marker(
                                    state = MarkerState(position = it),
                                    title = "Lokasi Anda",
                                    snippet = "Anda di sini"
                                )
                            }
                            
                            // Waste location markers
                            wasteLocations.forEach { location ->
                                Marker(
                                    state = MarkerState(position = location.latLng),
                                    title = location.name,
                                    snippet = location.type
                                )
                            }
                        }
                        
                        // Loading overlay
                        if (isLoadingLocation) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = EcoGreen
                                        )
                                        Text(
                                            "Mendapatkan lokasi...",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                        
                        // Permission info banner
                        if (!hasLocationPermission && !isLoadingLocation) {
                            Card(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                    Text(
                                        "Izin lokasi diperlukan untuk fitur ini",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }
                        
                        // My Location Button
                        FloatingActionButton(
                            onClick = {
                                if (hasLocationPermission) {
                                    if (!isLoadingLocation) {
                                        isLoadingLocation = true
                                        locationAddress = "Memperbarui lokasi Anda..."
                                        
                                        scope.launch {
                                            try {
                                                kotlinx.coroutines.withTimeout(15000L) {
                                                    val location = locationHelper.getCurrentLocation()
                                                    if (location != null) {
                                                        android.util.Log.d("WasteTrack", "Location updated: ${location.latitude}, ${location.longitude}, accuracy: ${location.accuracy}m")
                                                        currentLocation = LatLng(location.latitude, location.longitude)
                                                        
                                                        // Get address
                                                        locationAddress = "Mengambil nama lokasi..."
                                                        val address = locationHelper.getAddressFromLocation(location.latitude, location.longitude)
                                                        locationAddress = address
                                                        
                                                        cameraPositionState.animate(
                                                            update = CameraUpdateFactory.newLatLngZoom(
                                                                LatLng(location.latitude, location.longitude),
                                                                16f
                                                            ),
                                                            durationMs = 1000
                                                        )
                                                        android.widget.Toast.makeText(
                                                            context,
                                                            "Lokasi diperbarui: ${location.accuracy.toInt()}m akurasi",
                                                            android.widget.Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else {
                                                        android.util.Log.e("WasteTrack", "Location update failed: null")
                                                        locationAddress = "GPS tidak dapat menemukan lokasi"
                                                        android.widget.Toast.makeText(
                                                            context,
                                                            "Gagal menemukan lokasi. Coba keluar ruangan dan aktifkan mode akurasi tinggi.",
                                                            android.widget.Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }
                                            } catch (e: kotlinx.coroutines.TimeoutCancellationException) {
                                                android.util.Log.e("WasteTrack", "Location update timeout")
                                                locationAddress = if (currentLocation != null) {
                                                    val addr = locationHelper.getAddressFromLocation(
                                                        currentLocation!!.latitude,
                                                        currentLocation!!.longitude
                                                    )
                                                    addr
                                                } else {
                                                    "Timeout - GPS tidak merespon"
                                                }
                                                android.widget.Toast.makeText(
                                                    context,
                                                    "Timeout. Coba pindah ke outdoor dan aktifkan mode akurasi tinggi.",
                                                    android.widget.Toast.LENGTH_LONG
                                                ).show()
                                            }
                                            isLoadingLocation = false
                                        }
                                    }
                                } else {
                                    permissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp),
                            containerColor = Color.White,
                            contentColor = if (currentLocation != null) EcoGreen else Color.Gray,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 6.dp
                            )
                        ) {
                            if (isLoadingLocation) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = EcoGreen
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = "Lokasi Saya"
                                )
                            }
                        }
                    }
                }
            }
            
            item {
                // Header List
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Lokasi Terdekat",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            "${wasteLocations.size} lokasi",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // List Items
            items(wasteLocations.size) { index ->
                val location = wasteLocations[index]
                LocationItem(
                    location = location,
                    onNavigate = {
                        // Open in Google Maps
                        val uri = Uri.parse("google.navigation:q=${location.latLng.latitude},${location.latLng.longitude}")
                        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        } else {
                            // Fallback to web maps
                            val webUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${location.latLng.latitude},${location.latLng.longitude}")
                            context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
                        }
                    },
                    onMarkerClick = {
                        // Move camera to marker
                        scope.launch {
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newLatLngZoom(location.latLng, 17f),
                                durationMs = 1000
                            )
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun LocationItem(
    location: WasteLocation,
    onNavigate: () -> Unit,
    onMarkerClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = location.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Surface(
                    color = EcoGreen.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = location.type,
                        color = EcoGreen,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = location.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onMarkerClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = EcoGreen
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Lihat")
                }
                Button(
                    onClick = onNavigate,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = EcoGreen)
                ) {
                    Icon(
                        imageVector = Icons.Default.NearMe,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Arahkan")
                }
            }
        }
    }
}