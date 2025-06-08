package com.example.nachorestaurante.data.sources.datasources


import com.example.nachorestaurante.data.common.NetworkResult
import com.example.nachorestaurante.data.model.SimpleError
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber


abstract class BaseApiCall {
    inline fun < reified T > safeApiCall(apiCall: () -> Response<T>): NetworkResult<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                when {
                    body != null -> NetworkResult.Success(body)
                    else -> NetworkResult.Success(Unit as T)
                }
            } else {
                response.errorBody()?.let { errorBody ->
                    parseErrorResponse(errorBody)
                } ?: NetworkResult.Error("${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun <T> safeApiCallNoBody(apiCall: suspend () -> Response<T>): NetworkResult<Boolean> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                return NetworkResult.Success(true)
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            Timber.e(e.message, e)
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(errorMessage: String): NetworkResult<T> =
        NetworkResult.Error(errorMessage)

    fun <T> parseErrorResponse(errorBody: ResponseBody): NetworkResult<T> {
        return try {
            val errorBodyString = errorBody.string()
            val simpleError = Gson().fromJson(errorBodyString, SimpleError::class.java)
            NetworkResult.Error(simpleError.message)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

}

