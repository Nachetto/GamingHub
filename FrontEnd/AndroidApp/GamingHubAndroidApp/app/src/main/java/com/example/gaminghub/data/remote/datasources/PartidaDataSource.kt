package com.example.gaminghub.data.remote.datasources

import com.example.gaminghub.data.remote.service.PartidaService
import javax.inject.Inject

class PartidaDataSource @Inject constructor() : BaseApiCall() {
    suspend fun getPartidas(
        partidaService: PartidaService,
        username : String,
    ) = safeApiCall {
        partidaService.getPartidas(username)
    }

}