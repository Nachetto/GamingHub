package com.example.gaminghub.domain.usecases

import com.example.gaminghub.data.common.NetworkResult
import com.example.gaminghub.data.remote.security.Token
import com.example.gaminghub.data.repositorios.AuthRepository
import com.example.gaminghub.domain.modelo.UserAuth

import javax.inject.Inject;

class LoginUseCase @Inject constructor(
    private val authRepository:AuthRepository
) {
    suspend fun invoke(userAuth: UserAuth): NetworkResult<Token> {
        return authRepository.login(userAuth)
    }
}