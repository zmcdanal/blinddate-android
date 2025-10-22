package com.ethereal.network.storage

import android.net.Uri

interface StorageDataSource {
    // Placeholder API methods that I might use
    suspend fun uploadUserPhoto(uid: String, localUri: Uri): String
    suspend fun deleteUserPhoto(uid: String): Unit
    suspend fun getUserPhotoUrl(uid: String): String?
}