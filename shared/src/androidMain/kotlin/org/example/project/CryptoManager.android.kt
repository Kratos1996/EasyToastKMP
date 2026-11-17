package org.example.project

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

actual object CryptoManager {
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val GCM_IV_LENGTH = 12
    private const val GCM_TAG_LENGTH = 128

    actual fun encrypt(plainText: String?, secretKey: String): String {
        // 1. Handle null plain text
        if (plainText.isNullOrEmpty()) {
            return "Error: Plain text is empty. Nothing to encrypt."
        }
        // 2. Ensure key isn't just an empty string
        if (secretKey.isBlank()) {
            return "Error: Secret key cannot be empty."
        }

        val keyBytes = secretKey.toByteArray(Charsets.UTF_8)
        val validKey = ByteArray(32)
        System.arraycopy(keyBytes, 0, validKey, 0, minOf(keyBytes.size, 32))
        val keySpec = SecretKeySpec(validKey, ALGORITHM)

        val iv = ByteArray(GCM_IV_LENGTH)
        SecureRandom().nextBytes(iv)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec)

        val cipherText = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        val combined = ByteArray(iv.size + cipherText.size)
        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(cipherText, 0, combined, iv.size, cipherText.size)

        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    actual fun decrypt(cipherText: String?, secretKey: String): String {
        // 1. Handle null cipher text
        if (cipherText.isNullOrEmpty()) {
            return "Error: Cipher text is null. Nothing to decrypt."
        }
        if (secretKey.isBlank()) {
            return "Error: Secret key cannot be empty."
        }

        try {
            val combined = Base64.decode(cipherText, Base64.NO_WRAP)
            val iv = combined.copyOfRange(0, GCM_IV_LENGTH)
            val encryptedData = combined.copyOfRange(GCM_IV_LENGTH, combined.size)

            val keyBytes = secretKey.toByteArray(Charsets.UTF_8)
            val validKey = ByteArray(32)
            System.arraycopy(keyBytes, 0, validKey, 0, minOf(keyBytes.size, 32))
            val keySpec = SecretKeySpec(validKey, ALGORITHM)

            val cipher = Cipher.getInstance(TRANSFORMATION)
            val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec)

            val decryptedBytes = cipher.doFinal(encryptedData)
            return String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            return "Decryption Error: ${e.message}"
        }
    }
}