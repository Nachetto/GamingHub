package com.example.nachorestaurante.data.sources.service

import com.example.nachorestaurante.data.sources.security.Token
import com.example.nachorestaurante.domain.modelo.UserAuth
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {

    @POST("login")
    suspend fun login (@Body authenticationUser : UserAuth) : Response<Token>

    @POST("register/{username}/{password}/{phone}")
    suspend fun register (@Path("username") username: String,@Path("password") password: String,@Path("phone")phone: Int) : Response<Unit>

    @GET("refresh")
    suspend fun refreshToken(
        @Header("Authorization") token: String,
    ): Response<String>
}