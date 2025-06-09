package com.example.gaminghub.framework.pantallaLogin

sealed class LoginEvent {
    data class Login(val username: String, val password: String) : LoginEvent()
    data class ServerLoginWithGoogleDetails(
        val tokenId: String,
        val email : String,
        val googleId: String,
        val username: String
    ) : LoginEvent()
    object NavigateToMain : LoginEvent()
    object NavigateToRegister : LoginEvent()
    object ShowError : LoginEvent()
    object NoLongerLoading : LoginEvent()
}