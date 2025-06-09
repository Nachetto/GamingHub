package com.example.gaminghub.data.remote.datasources

import com.example.gaminghub.data.remote.service.AmigosService
import javax.inject.Inject

class AmigosDataSource @Inject constructor() : BaseApiCall() {
    suspend fun getAmigos(
        amigosService: AmigosService,
        username : String,
    ) = safeApiCall {
        amigosService.getAmigos(username)
    }

}