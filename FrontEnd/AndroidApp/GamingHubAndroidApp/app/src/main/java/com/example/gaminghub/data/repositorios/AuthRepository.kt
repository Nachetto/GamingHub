package com.example.gaminghub.data.repositorios

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.gaminghub.data.common.NetworkResult
import com.example.gaminghub.data.remote.datasources.AuthenticationDatasource
import com.example.gaminghub.data.remote.security.Token
import com.example.gaminghub.data.remote.service.AuthService
import com.example.gaminghub.di.dataStore
import com.example.gaminghub.domain.modelo.UserAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authenticationDatasource: AuthenticationDatasource,
    private val authenticationService: AuthService,
    @ApplicationContext private val context: Context
) {
    suspend fun register (authenticationUser: UserAuth) : NetworkResult<Unit>
            = authenticationDatasource.register(authenticationService,authenticationUser)

    suspend fun login(authenticationUser: UserAuth): NetworkResult<Token> =
        authenticationDatasource.login(authenticationService,authenticationUser)

    suspend fun loginWithGoogle(idToken: String): NetworkResult<Token> =
        authenticationDatasource.loginWithGoogle(authenticationService,idToken)



    private val REFRESH_TOKEN = stringPreferencesKey("jwt_refresh")
    private val ACCESS_TOKEN = stringPreferencesKey("jwt_access")

    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN]
        }
    }

    suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = token
        }
    }

    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }
    }

    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
        }
    }
}