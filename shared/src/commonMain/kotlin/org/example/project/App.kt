package org.example.project

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.toast.DemoScreen
import org.example.project.toast.ToastHost

@Composable
fun App() {
    val darkTheme = isSystemInDarkTheme()

    MaterialTheme(
        colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
    ) {
        ToastHost {
            DemoScreen()
        }
    }
}

//    // States for Encrypting
//    var inputText by remember { mutableStateOf("") }
//    var encryptedResult by remember { mutableStateOf("") }
//
//    // NEW: State for Manual Decrypting (Tamper Test)
//    var customCipherText by remember { mutableStateOf("") }
//    var manualDecryptResult by remember { mutableStateOf("") }
//
//    val secretKey = "12345678901234567890123456789012"
//
//    MaterialTheme {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//                .verticalScroll(rememberScrollState()),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Text("AES-256 Encryption", style = MaterialTheme.typography.headlineMedium)
//
//            // --- SECTION 1: ENCRYPT ---
//            OutlinedTextField(
//                value = inputText,
//                onValueChange = { inputText = it },
//                label = { Text("Enter text to encrypt") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Button(
//                onClick = {
//                    val encrypted = CryptoManager.encrypt(inputText, secretKey)
//                    encryptedResult = encrypted
//                    // Auto-fill the tamper test field so you can easily copy it
//                    customCipherText = encrypted
//                },
//                modifier = Modifier.fillMaxWidth(),
//                enabled = inputText.isNotBlank()
//            ) {
//                Text("Encrypt")
//            }
//
//            if (encryptedResult.isNotEmpty()) {
//                Text("Encrypted Text (Copy this):", fontWeight = FontWeight.Bold)
//                Text(encryptedResult, style = MaterialTheme.typography.bodyMedium)
//            }
//
//            Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//            // --- SECTION 2: TAMPER TEST ---
//            Text("Tamper Test (Decrypt)", style = MaterialTheme.typography.titleLarge)
//
//            OutlinedTextField(
//                value = customCipherText,
//                onValueChange = { customCipherText = it },
//                label = { Text("Paste encrypted text here and change 1 character") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Button(
//                onClick = {
//                    // This will try to decrypt whatever is in the customCipherText field
//                    manualDecryptResult = CryptoManager.decrypt(customCipherText, secretKey)
//                },
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
//            ) {
//                Text("Attempt Decryption")
//            }
//
//            if (manualDecryptResult.isNotEmpty()) {
//                Text("Decryption Result:", fontWeight = FontWeight.Bold)
//                Text(manualDecryptResult)
//            }
//        }
//    }
