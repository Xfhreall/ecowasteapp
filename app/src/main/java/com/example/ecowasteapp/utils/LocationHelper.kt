package com.example.ecowasteapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationHelper(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val geocoder = Geocoder(context, Locale("id", "ID"))

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getCurrentLocation(): Location? {
        if (!hasLocationPermission()) {
            return null
        }

        return try {
            // PRIORITAS 1: Coba last location dulu (lebih cepat dan reliable)
            val lastLocation = fusedLocationClient.lastLocation.await()
            
            if (lastLocation != null && isLocationRecent(lastLocation)) {
                // Jika ada last location yang masih fresh (<5 menit), return langsung
                android.util.Log.d("LocationHelper", "Using last known location (age: ${getLocationAge(lastLocation)}s)")
                return lastLocation
            }
            
            // PRIORITAS 2: Jika tidak ada last location atau sudah lama, request current location
            // Gunakan HIGH_ACCURACY untuk GPS yang lebih presisi
            val cancellationTokenSource = CancellationTokenSource()
            android.util.Log.d("LocationHelper", "Requesting fresh location...")
            
            val newLocation = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, // Gunakan GPS untuk akurasi maksimal
                cancellationTokenSource.token
            ).await()
            
            newLocation ?: lastLocation // Fallback ke last location jika gagal
        } catch (e: Exception) {
            e.printStackTrace()
            android.util.Log.e("LocationHelper", "Error getting location: ${e.message}")
            
            // Last resort: coba ambil last location lagi
            try {
                fusedLocationClient.lastLocation.await()
            } catch (e2: Exception) {
                null
            }
        }
    }
    
    private fun isLocationRecent(location: Location): Boolean {
        val ageInSeconds = (System.currentTimeMillis() - location.time) / 1000
        return ageInSeconds < 300 // 5 menit
    }
    
    private fun getLocationAge(location: Location): Long {
        return (System.currentTimeMillis() - location.time) / 1000
    }
    
    suspend fun getAddressFromLocation(latitude: Double, longitude: Double): String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ (API 33+)
                suspendCoroutine { continuation ->
                    geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                        val address = addresses.firstOrNull()
                        continuation.resume(formatAddress(address))
                    }
                }
            } else {
                // Android 12 and below
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                formatAddress(addresses?.firstOrNull())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Lat: ${String.format("%.4f", latitude)}, Lng: ${String.format("%.4f", longitude)}"
        }
    }
    
    private fun formatAddress(address: Address?): String {
        return if (address != null) {
            // Build address string dengan prioritas nama jalan dan area
            val parts = mutableListOf<String>()
            
            // 1. Nama jalan lengkap (thoroughfare) atau feature name
            address.thoroughfare?.let { 
                parts.add(it)
            } ?: address.featureName?.let {
                // Jika tidak ada nama jalan, coba feature name (tapi skip jika hanya nomor/koordinat)
                if (!it.contains(",") && !it.matches(Regex("^[-\\d.]+$"))) {
                    parts.add(it)
                }
            }
            
            // 2. Kelurahan/Desa (sub-locality)
            address.subLocality?.let { 
                if (!parts.contains(it)) parts.add(it) 
            }
            
            // 3. Kecamatan (locality) atau Kota (subAdminArea)
            val areaName = address.locality ?: address.subAdminArea
            areaName?.let {
                if (!parts.contains(it)) parts.add(it)
            }
            
            // 4. Provinsi (adminArea) - hanya tambahkan jika belum ada kota
            if (parts.size < 3) {
                address.adminArea?.let {
                    if (!parts.contains(it)) parts.add(it)
                }
            }
            
            // Format akhir
            when {
                parts.size >= 2 -> {
                    // Tampilkan 2-3 bagian pertama untuk lebih deskriptif
                    parts.take(3).joinToString(", ")
                }
                parts.size == 1 -> {
                    // Jika hanya 1 bagian, tambahkan info kota/provinsi
                    val extra = address.subAdminArea ?: address.adminArea
                    if (extra != null && extra != parts[0]) {
                        "${parts[0]}, $extra"
                    } else {
                        parts[0]
                    }
                }
                else -> {
                    // Fallback ke format tradisional jika parsing gagal
                    buildString {
                        append(address.subAdminArea ?: address.locality ?: "")
                        if (address.adminArea != null) {
                            if (isNotEmpty()) append(", ")
                            append(address.adminArea)
                        }
                        if (isEmpty()) {
                            append("Lat: ${String.format("%.4f", address.latitude)}, Lng: ${String.format("%.4f", address.longitude)}")
                        }
                    }
                }
            }
        } else {
            "Lokasi tidak ditemukan"
        }
    }
}
