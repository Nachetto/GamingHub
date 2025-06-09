package com.example.gaminghub.data.repositorios

import com.example.gaminghub.data.remote.datasources.PartidaDataSource
import com.example.gaminghub.data.remote.service.PartidaService
import javax.inject.Inject


class PartidasRepository @Inject constructor(
    private val partidaDataSource: PartidaDataSource,
    private val authenticationService: PartidaService,
) {
    suspend fun getPartidas(userName: String) = partidaDataSource.getPartidas(authenticationService,userName)
}