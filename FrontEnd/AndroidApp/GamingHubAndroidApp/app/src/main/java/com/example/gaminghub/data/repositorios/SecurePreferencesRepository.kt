package com.example.gaminghub.data.repositorios

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.gaminghub.data.remote.security.CryptoHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurePreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val cryptoHelper: CryptoHelper
) {
    companion object {
        private val ENCRYPTED_CREDENTIALS = stringPreferencesKey("encrypted_credentials_marker")
    }

    suspend fun saveCredentials(username: String, password: String) {
        val combined = "$username:$password"
        cryptoHelper.encryptAndSaveData(combined)
        dataStore.edit { preferences ->
            preferences[ENCRYPTED_CREDENTIALS] = "present"
        }
    }

    //para cuando hagas login mas adelante y quieras obtener las credenciales
    fun getCredentials(): Pair<String?, String?> {
        return try {
            val decrypted = cryptoHelper.readAndDecryptData()
            val parts = decrypted.split(":")
            if (parts.size == 2) Pair(parts[0], parts[1]) else Pair(null, null)
        } catch (e: Exception) {
            Pair(null, null)
        }
    }

    fun getUsername(): String? {
        return try {
            val decrypted = cryptoHelper.readAndDecryptData()
            val parts = decrypted.split(":")
            if (parts.isNotEmpty()) parts[0] else null
        } catch (e: Exception) {
            null
        }
    }
}