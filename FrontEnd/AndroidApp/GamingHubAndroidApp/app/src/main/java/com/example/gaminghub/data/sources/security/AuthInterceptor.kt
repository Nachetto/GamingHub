package com.example.nachorestaurante.data.sources.security

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val dataStoreRepository: DataStoreClass,
    private val refreshTokenProvider: suspend () -> String?,
    private val refreshAccessToken: suspend (String) -> String?
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = runBlocking { dataStoreRepository.getAccessToken().first() }

        // Añadir el access token a la solicitud
        val authenticatedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(authenticatedRequest)

        if (response.code == 401) { // Token caducado
            response.close()

            val refreshToken = runBlocking { refreshTokenProvider() }
            if (refreshToken != null) {
                val newAccessToken = runBlocking { refreshAccessToken(refreshToken) }
                if (newAccessToken != null) {
                    runBlocking { dataStoreRepository.saveAccessToken(newAccessToken) }

                    // Reintentar la solicitud con el nuevo token
                    val newRequest = originalRequest.newBuilder()
                        .addHeader("Authorization", "Bearer $newAccessToken")
                        .build()
                    return chain.proceed(newRequest)
                }
            }
        }

        return response
    }
}