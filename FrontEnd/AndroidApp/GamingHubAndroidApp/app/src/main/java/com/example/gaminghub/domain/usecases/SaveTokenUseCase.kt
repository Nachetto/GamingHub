package com.example.gaminghub.domain.usecases

import com.example.gaminghub.data.remote.security.Token
import com.example.gaminghub.data.repositorios.AuthRepository
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val dataStoreRepository: AuthRepository,
) {
    suspend fun invoke (token : Token) {
        dataStoreRepository.saveAccessToken(token.access)
        dataStoreRepository.saveRefreshToken(token.refresh)
    }
}