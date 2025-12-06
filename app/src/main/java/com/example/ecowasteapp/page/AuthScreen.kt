package com.example.ecowasteapp.page

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ecowasteapp.components.EcoGreen

@Composable
fun AuthScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login (Mode Demo)",
            style = MaterialTheme.typography.headlineMedium,
            color = EcoGreen
        )
        Text("Masukkan sembarang email & password untuk masuk.", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email / Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // LOGIKA DUMMY: Asal tidak kosong, boleh masuk
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    onLoginSuccess()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = EcoGreen),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Masuk")
        }
    }
}