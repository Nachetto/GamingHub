package com.example.gaminghub.data.remote.service

import com.example.gaminghub.data.model.AmigoDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AmigosService {
    @GET("friends/getAmigosFromUsername")
    suspend fun getAmigos(
        @Query("username") username: String
    ): Response<List<AmigoDTO>>
}