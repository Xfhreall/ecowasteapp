package com.example.ecowasteapp.repository

import android.net.Uri
import com.example.ecowasteapp.model.UserPost
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class PostRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val postsCollection = firestore.collection("posts")

    // Real-time listener untuk posts
    fun getPosts(): Flow<List<UserPost>> = callbackFlow {
        val listener = postsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val posts = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        UserPost(
                            id = doc.id,
                            userId = doc.getString("userId") ?: "",
                            username = doc.getString("username") ?: "Unknown",
                            caption = doc.getString("caption") ?: "",
                            imageUrl = doc.getString("imageUrl"),
                            timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(posts)
            }

        awaitClose { listener.remove() }
    }

    // Upload gambar ke Storage
    private suspend fun uploadImage(uri: Uri): String {
        val fileName = "posts/${UUID.randomUUID()}.jpg"
        val storageRef = storage.reference.child(fileName)
        storageRef.putFile(uri).await()
        return storageRef.downloadUrl.await().toString()
    }

    // Create post
    suspend fun createPost(
        userId: String,
        username: String,
        caption: String,
        imageUri: Uri?
    ): Result<String> {
        return try {
            val imageUrl = imageUri?.let { uploadImage(it) }
            
            val postData = hashMapOf(
                "userId" to userId,
                "username" to username,
                "caption" to caption,
                "imageUrl" to imageUrl,
                "timestamp" to System.currentTimeMillis()
            )

            val docRef = postsCollection.add(postData).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Delete post
    suspend fun deletePost(postId: String, imageUrl: String?): Result<Unit> {
        return try {
            // Hapus gambar dari storage jika ada
            imageUrl?.let {
                try {
                    val storageRef = storage.getReferenceFromUrl(it)
                    storageRef.delete().await()
                } catch (e: Exception) {
                    // Ignore if image delete fails
                }
            }

            // Hapus dokumen dari Firestore
            postsCollection.document(postId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
