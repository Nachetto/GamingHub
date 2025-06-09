package com.example.gaminghub.data.remote.datasources

import com.example.gaminghub.data.common.NetworkResult
import com.example.gaminghub.data.remote.security.Token
import com.example.gaminghub.data.remote.service.AuthService
import com.example.gaminghub.domain.modelo.UserAuth
import javax.inject.Inject

class AuthenticationDatasource @Inject constructor() : BaseApiCall() {

    suspend fun register(
        authService: AuthService,
        authenticationUser: UserAuth
    ): NetworkResult<Unit> =
        safeApiCall { authService.register(authenticationUser.username, authenticationUser.password, authenticationUser.phone) }

    suspend fun login(
        authService: AuthService,
        authenticationUser: UserAuth
    ): NetworkResult<Token> =
        safeApiCall { authService.login(authenticationUser) }

    suspend fun loginWithGoogle(
        authService: AuthService,
        idToken: String
    ): NetworkResult<Token> =
        safeApiCall { authService.loginWithGoogle(idToken) }
}