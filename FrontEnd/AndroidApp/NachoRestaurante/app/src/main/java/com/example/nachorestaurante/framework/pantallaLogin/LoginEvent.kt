package com.example.nachorestaurante.framework.pantallaLogin

sealed class LoginEvent {
    data class Login(val username: String, val password: String) : LoginEvent()
    object Register : LoginEvent()
    object NavigateToMain : LoginEvent()
    object NavigateToRegister : LoginEvent()
    object ShowError : LoginEvent()
}