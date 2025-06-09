package com.example.gaminghub.domain.usecases

import com.example.gaminghub.data.common.NetworkResult
import com.example.gaminghub.data.model.AmigoDTO
import com.example.gaminghub.data.repositorios.AmigosRepository
import javax.inject.Inject

class GetAmigosUseCase @Inject constructor(
    private val amigosRepository: AmigosRepository
) {
    suspend fun invoke(username: String):NetworkResult<List<AmigoDTO>> {
        return amigosRepository.getAmigos(username)
    }
}