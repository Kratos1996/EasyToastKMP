package org.example.project


expect object CryptoManager {
    fun encrypt(plainText: String?, secretKey: String): String
    fun decrypt(cipherText: String?, secretKey: String): String
}