package com.example.gaminghub.data.remote.service

import com.example.gaminghub.data.model.PartidaDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PartidaService {
    @GET("partidas/getAnfitrionedPartidasFromUsername")
    suspend fun getPartidas(
        @Query("username") username: String
    ): Response<List<PartidaDTO>>
}