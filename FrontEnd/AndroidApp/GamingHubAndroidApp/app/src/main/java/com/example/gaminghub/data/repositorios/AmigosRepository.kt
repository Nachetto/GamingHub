package com.example.gaminghub.data.repositorios

import com.example.gaminghub.data.remote.datasources.AmigosDataSource
import com.example.gaminghub.data.remote.service.AmigosService
import javax.inject.Inject

class AmigosRepository @Inject constructor(
    private val amigosDataSource: AmigosDataSource,
    private val authenticationService: AmigosService,
) {
    suspend fun getAmigos(userName: String) = amigosDataSource.getAmigos(authenticationService,userName)
}