package com.example.gaminghub.data.remote.security

import com.example.gaminghub.data.repositorios.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider

class AuthInterceptor @Inject constructor(
    private val authRepositoryProvider: Provider<AuthRepository>
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authRepository = authRepositoryProvider.get()
        val token = runBlocking {
            authRepository.getAccessToken().first()
        }
        val request = chain.request().newBuilder()
        if (chain.request().headers["Authorization"] == null) {
            request.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(request.build())
    }
}