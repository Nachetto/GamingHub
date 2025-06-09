package com.example.gaminghub.data.remote.di

import com.example.gaminghub.data.remote.security.AuthAuthenticator
import com.example.gaminghub.data.remote.security.AuthInterceptor
import com.example.gaminghub.data.remote.security.DataStoreClass
import com.example.gaminghub.data.remote.service.AuthService
import com.example.gaminghub.data.remote.service.PartidaService
import com.example.gaminghub.data.remote.service.old.CustomerService
import com.example.gaminghub.data.remote.service.old.OrderService
import com.example.gaminghub.data.repositorios.AuthRepository
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider
import javax.inject.Qualifier
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // SERVICES //
    @Provides
    fun provideAuthenticationService(@MainRetrofit retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideCustomerService(@MainRetrofit retrofit: Retrofit): CustomerService {
        return retrofit.create(CustomerService::class.java)
    }

    @Provides
    @Singleton
    fun provideOrderService(@MainRetrofit retrofit: Retrofit): OrderService {
        return retrofit.create(OrderService::class.java)
    }

    @Provides
    @Singleton
    fun providePartidaService(@MainRetrofit retrofit: Retrofit): PartidaService {
        return retrofit.create(PartidaService::class.java)
    }

    // BASE SETUP //

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainRetrofit

    @Provides
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    fun provideAuthInterceptor(authRepositoryProvider: Provider<AuthRepository>): AuthInterceptor {
        return AuthInterceptor(authRepositoryProvider)
    }

    @Singleton
    @Provides
    fun provideAuthAuthenticator(dataStoreClass: DataStoreClass, usersService: Lazy<AuthService>): AuthAuthenticator =
        AuthAuthenticator(dataStoreClass,usersService)

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        authenticator: AuthAuthenticator,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .authenticator(authenticator)
            .build()
    }

    @Provides
    @MainRetrofit
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.129:8080/api/") // <-- Main API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

}