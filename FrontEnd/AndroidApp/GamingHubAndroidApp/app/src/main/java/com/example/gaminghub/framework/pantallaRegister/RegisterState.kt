package com.example.gaminghub.framework.pantallaRegister

import com.example.gaminghub.domain.modelo.UserAuth

sealed class RegisterState {
    data class Success(val username: String) : RegisterState()
    data class Error(val message: String) : RegisterState()
    data class Loading(val isLoading: Boolean) : RegisterState()
}