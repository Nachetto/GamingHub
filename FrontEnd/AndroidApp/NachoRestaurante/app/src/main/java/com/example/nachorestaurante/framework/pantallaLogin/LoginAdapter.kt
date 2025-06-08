package com.example.nachorestaurante.framework.pantallaLogin

class LoginAdapter(
    val context: LoginActivity,
    val actions: LoginActions
) {
    interface LoginActions {
        fun onLogin(username: String, password: String)
        fun onRegister()
    }

    // Implement the necessary methods for the adapter here
    // For example, onCreateViewHolder, onBindViewHolder, getItemCount, etc.
}