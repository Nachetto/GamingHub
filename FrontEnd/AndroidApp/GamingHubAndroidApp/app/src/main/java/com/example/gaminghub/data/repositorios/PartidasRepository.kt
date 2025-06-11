package com.example.gaminghub.data.repositorios

import android.content.Context
import com.example.gaminghub.data.remote.datasources.PartidaDataSource
import com.example.gaminghub.data.remote.service.PartidaService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class PartidasRepository @Inject constructor(
    private val authenticationDatasource: PartidaDataSource,
    private val authenticationService: PartidaService,
    @ApplicationContext private val context: Context
) {
    suspend fun getPartidas(userName: String) = authenticationDatasource.getPartidas(authenticationService,userName)
}