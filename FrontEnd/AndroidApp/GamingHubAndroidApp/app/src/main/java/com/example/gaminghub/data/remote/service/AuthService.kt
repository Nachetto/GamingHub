package com.example.gaminghub.data.remote.service

import com.example.gaminghub.data.remote.security.Token
import com.example.gaminghub.domain.modelo.UserAuth
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthService {

    @POST("auth/login")
    suspend fun login (@Body authenticationUser : UserAuth) : Response<Token>

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("phone") phone: Int
    ): Response<Unit>

    @GET("auth/refresh-token")
    suspend fun refreshToken(
        @Header("Authorization") token: String,
    ): Response<String>

    @GET("auth/google-login")
    suspend fun loginWithGoogle(
        @Query("idToken") idToken: String
    ): Response<Token>
}