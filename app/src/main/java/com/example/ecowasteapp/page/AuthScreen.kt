package com.example.ecowasteapp.page

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ecowasteapp.components.EcoGreen
import com.example.ecowasteapp.repository.AuthRepository
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    authRepository: AuthRepository,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isLogin) "Masuk" else "Daftar",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = EcoGreen
        )
        Text(
            text = if (isLogin) "Masuk ke akun Anda" else "Buat akun baru",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                errorMessage = null
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isError = errorMessage != null
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                errorMessage = null
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isError = errorMessage != null
        )
        
        // Error message
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    errorMessage = "Email dan password tidak boleh kosong"
                    return@Button
                }
                
                isLoading = true
                errorMessage = null
                
                scope.launch {
                    val result = if (isLogin) {
                        authRepository.login(email, password)
                    } else {
                        authRepository.register(email, password)
                    }
                    
                    isLoading = false
                    
                    result.onSuccess {
                        onLoginSuccess()
                    }.onFailure { exception ->
                        errorMessage = when {
                            exception.message?.contains("no user record") == true -> 
                                "Email tidak terdaftar"
                            exception.message?.contains("password is invalid") == true -> 
                                "Password salah"
                            exception.message?.contains("email address is already") == true -> 
                                "Email sudah terdaftar"
                            exception.message?.contains("badly formatted") == true -> 
                                "Format email tidak valid"
                            exception.message?.contains("weak-password") == true -> 
                                "Password minimal 6 karakter"
                            else -> exception.message ?: "Terjadi kesalahan"
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = EcoGreen),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(if (isLogin) "Masuk" else "Daftar")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(onClick = { 
            isLogin = !isLogin
            errorMessage = null
        }) {
            Text(
                text = if (isLogin) "Belum punya akun? Daftar" else "Sudah punya akun? Masuk",
                color = EcoGreen
            )
        }
    }
}