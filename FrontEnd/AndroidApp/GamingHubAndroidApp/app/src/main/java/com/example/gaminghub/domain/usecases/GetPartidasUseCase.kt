package com.example.gaminghub.domain.usecases

import com.example.gaminghub.data.common.NetworkResult
import com.example.gaminghub.data.model.PartidaDTO
import com.example.gaminghub.data.repositorios.PartidasRepository
import javax.inject.Inject


class GetPartidasUseCase @Inject constructor(
    private val partidasRepository: PartidasRepository
) {
    suspend fun invoke(username: String):NetworkResult<List<PartidaDTO>> {
        return partidasRepository.getPartidas(username)
    }
}