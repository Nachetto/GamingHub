package com.example.gaminghub.framework.pantallaRegister

sealed class RegisterEvent {
    data class Register(
        val username: String,
        val phone: Int,
        val password: String
    ) : RegisterEvent()
    object NoLongerLoading : RegisterEvent()
    data class ShowError(val error: String) : RegisterEvent()
}