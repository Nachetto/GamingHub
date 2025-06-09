package com.example.gaminghub.framework.pantallaLogin

import com.example.gaminghub.domain.modelo.UserAuth

sealed class LoginState {
    data class Success(val authenticatedUser: UserAuth) : LoginState()
    data class Error(val message: String) : LoginState()
    data class Loading(val isLoading: Boolean) : LoginState()
}