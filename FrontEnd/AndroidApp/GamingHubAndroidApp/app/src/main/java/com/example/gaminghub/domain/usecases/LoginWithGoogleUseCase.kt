package com.example.gaminghub.domain.usecases

import com.example.gaminghub.data.common.NetworkResult
import com.example.gaminghub.data.remote.security.Token
import com.example.gaminghub.data.repositorios.AuthRepository
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun invoke(idToken: String):NetworkResult<Token> {
        return authRepository.loginWithGoogle(idToken)
    }
}