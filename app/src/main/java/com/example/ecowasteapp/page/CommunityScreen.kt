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
import com.example.ecowasteapp.repository.AuthRepository
import com.example.ecowasteapp.repository.PostRepository
import kotlinx.coroutines.launch

@Composable
fun CommunityScreen() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val authRepository = remember { AuthRepository(context) }
    val postRepository = remember { PostRepository() }
    val scope = rememberCoroutineScope()
    
    // Real-time posts dari Firestore
    val posts by postRepository.getPosts().collectAsState(initial = emptyList())
    
    var showDialog by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    var deletePostId by remember { mutableStateOf<Pair<String, String?>?>(null) }
    
    val currentUserId = authRepository.getCurrentUserId()
    val currentUserEmail = authRepository.getCurrentUserEmail() ?: "Unknown"

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = EcoGreen, 
                contentColor = Color.White
            ) { 
                Icon(Icons.Default.Add, contentDescription = "Tambah Post") 
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color(0xFFF5F5F5))) {
            GreenHeader(
                title = "Komunitas", 
                subtitle = "Bagikan aksi hijau Anda"
            ) {}

            if (posts.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Belum ada postingan",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Jadilah yang pertama berbagi!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(posts, key = { it.id }) { post ->
                        PostItem(
                            post = post,
                            canDelete = post.userId == currentUserId,
                            onDelete = { 
                                deletePostId = Pair(post.id, post.imageUrl)
                            }
                        )
                    }
                }
            }
        }

        // Upload Dialog
        if (showDialog) {
            UploadDialog(
                isLoading = isUploading,
                onDismiss = { showDialog = false },
                onUpload = { caption, uri ->
                    isUploading = true
                    scope.launch {
                        val result = postRepository.createPost(
                            userId = currentUserId ?: "",
                            username = currentUserEmail,
                            caption = caption,
                            imageUri = uri
                        )
                        
                        isUploading = false
                        
                        result.onSuccess {
                            showDialog = false
                        }.onFailure {
                            // Handle error (bisa tambahkan Snackbar)
                        }
                    }
                }
            )
        }
        
        // Delete Confirmation Dialog
        deletePostId?.let { (postId, imageUrl) ->
            AlertDialog(
                onDismissRequest = { deletePostId = null },
                title = { Text("Hapus Postingan") },
                text = { Text("Apakah Anda yakin ingin menghapus postingan ini?") },
                confirmButton = {
                    Button(
                        onClick = {
                            scope.launch {
                                postRepository.deletePost(postId, imageUrl)
                                deletePostId = null
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Hapus")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deletePostId = null }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}

@Composable
fun PostItem(post: UserPost, canDelete: Boolean, onDelete: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = post.username,
                        fontWeight = FontWeight.Bold,
                        color = EcoGreen,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = formatTimestamp(post.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                
                if (canDelete) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete, 
                            contentDescription = "Hapus", 
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tampilkan Gambar jika ada
            if (post.imageUrl != null) {
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            Text(
                text = post.caption,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Helper function untuk format timestamp
private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60_000 -> "Baru saja"
        diff < 3_600_000 -> "${diff / 60_000} menit lalu"
        diff < 86_400_000 -> "${diff / 3_600_000} jam lalu"
        diff < 604_800_000 -> "${diff / 86_400_000} hari lalu"
        else -> {
            val date = java.util.Date(timestamp)
            java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale("id", "ID")).format(date)
        }
    }
}

@Composable
fun UploadDialog(
    isLoading: Boolean,
    onDismiss: () -> Unit, 
    onUpload: (String, Uri?) -> Unit
) {
    var caption by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { 
        imageUri = it 
    }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Buat Postingan Baru") },
        text = {
            Column {
                OutlinedTextField(
                    value = caption,
                    onValueChange = { caption = it },
                    label = { Text("Caption") },
                    placeholder = { Text("Ceritakan aksi hijau Anda...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    enabled = !isLoading
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                OutlinedButton(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (imageUri == null) "Tambah Foto (Opsional)" else "Ganti Foto")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    if (caption.isNotEmpty()) {
                        onUpload(caption, imageUri)
                    }
                },
                enabled = caption.isNotEmpty() && !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = EcoGreen)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text("Posting")
                }
            }
        },
        dismissButton = { 
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) { 
                Text("Batal") 
            } 
        }
    )
}