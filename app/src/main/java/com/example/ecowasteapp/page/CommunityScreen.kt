package com.example.ecowasteapp.page

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ecowasteapp.components.EcoGreen
import com.example.ecowasteapp.components.GreenHeader
import com.example.ecowasteapp.model.UserPost
import java.util.UUID

@Composable
fun CommunityScreen() {
    // INI DATABASE PURA-PURA (Hanya tersimpan di RAM)
    // Nanti temanmu akan mengganti bagian ini dengan query Firebase
    val posts = remember {
        mutableStateListOf(
            UserPost("1", "Budi Santoso", "Habis bersihin selokan depan rumah nih!", null, System.currentTimeMillis()),
            UserPost("2", "Siti Aminah", "Yuk pilah sampah plastik!", null, System.currentTimeMillis())
        )
    }

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = EcoGreen, contentColor = Color.White
            ) { Icon(Icons.Default.Add, contentDescription = "Tambah") }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color(0xFFF5F5F5))) {
            GreenHeader(title = "Komunitas", subtitle = "Bagikan aksimu (Mode Demo)") {}

            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(posts) { post ->
                    DummyPostItem(
                        post = post,
                        onDelete = { posts.remove(post) } // Hapus dari list lokal
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        if (showDialog) {
            DummyUploadDialog(
                onDismiss = { showDialog = false },
                onUpload = { caption, uri ->
                    // TAMBAH DATA KE LIST LOKAL
                    val newPost = UserPost(
                        id = UUID.randomUUID().toString(),
                        username = "Saya (User Demo)", // Hardcode nama user
                        caption = caption,
                        imageUri = uri, // Simpan URI lokal sementara
                        timestamp = System.currentTimeMillis()
                    )
                    posts.add(0, newPost) // Tambah ke paling atas
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun DummyPostItem(post: UserPost, onDelete: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(post.username, fontWeight = FontWeight.Bold, color = EcoGreen)
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red)
                }
            }

            // Tampilkan Gambar jika ada
            if (post.imageUri != null) {
                AsyncImage(
                    model = post.imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(post.caption)
        }
    }
}

@Composable
fun DummyUploadDialog(onDismiss: () -> Unit, onUpload: (String, Uri?) -> Unit) {
    var caption by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imageUri = it }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Buat Postingan Baru") },
        text = {
            Column {
                OutlinedTextField(
                    value = caption, onValueChange = { caption = it },
                    label = { Text("Caption") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (imageUri != null) {
                    AsyncImage(model = imageUri, contentDescription = null, modifier = Modifier.size(100.dp))
                }
                Button(onClick = { launcher.launch("image/*") }) {
                    Text(if (imageUri == null) "Pilih Foto" else "Ganti Foto")
                }
            }
        },
        confirmButton = {
            Button(onClick = { onUpload(caption, imageUri) }) { Text("Posting") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}