package com.example.nachorestaurante.framework.pantallaLogin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LoginViewModel@Inject constructor(
    private val loginUseCase: LoginUseCase

) : ViewModel() {

    private val _error = MutableLiveData<String>()

    fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Login -> login(event.username, event.password)
            LoginEvent.Register -> register()
            LoginEvent.NavigateToMain -> navigateToMain()
            LoginEvent.NavigateToRegister -> navigateToRegister()
            LoginEvent.ShowError -> showError()
        }
    }

    private fun login(username: String, password: String) {
        // Implement login logic using loginUseCase
        // Update _error if login fails
    }
    private fun register() {
        // Implement registration logic using registerUseCase
        // Update _error if registration fails
    }
    private fun navigateToMain() {
        // Logic to navigate to the main screen
    }
    private fun navigateToRegister() {
        // Logic to navigate to the registration screen
    }
    private fun showError() {
        // Logic to show error message
        _error.value = "An error occurred"
    }

    fun getError(): MutableLiveData<String> {
        return _error
    }
}