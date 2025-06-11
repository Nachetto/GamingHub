package com.example.gaminghub.domain.usecases

import com.example.gaminghub.data.common.NetworkResult
import com.example.gaminghub.data.remote.security.Token
import com.example.gaminghub.data.repositorios.AuthRepository
import com.example.gaminghub.domain.modelo.UserAuth
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository:AuthRepository
) {
    suspend fun invoke(userAuth: UserAuth): NetworkResult<Unit> {
        return authRepository.register(userAuth)
    }
}