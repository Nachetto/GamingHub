package com.example.gaminghub.framework.pantallaLogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gaminghub.data.common.NetworkResult
import com.example.gaminghub.data.repositorios.SecurePreferencesRepository
import com.example.gaminghub.di.IoDispatcher
import com.example.gaminghub.domain.modelo.UserAuth
import com.example.gaminghub.domain.usecases.LoginUseCase
import com.example.gaminghub.domain.usecases.LoginWithGoogleUseCase
import com.example.gaminghub.domain.usecases.SaveTokenUseCase
import com.example.gaminghub.framework.pantallaLogin.LoginState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val loginUseCase: LoginUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val securePreferencesRepository: SecurePreferencesRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState= MutableLiveData<LoginState>()
    val uiState: LiveData<LoginState> get() = _uiState
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    //cuando se llamaa al event, se ejecuta el metodo correspondiente
    fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Login -> login(event.username, event.password)
            is LoginEvent.ServerLoginWithGoogleDetails-> googleLogin(event.tokenId, event.email, event.googleId, event.username)
            LoginEvent.NavigateToMain -> navigateToMain()
            LoginEvent.NavigateToRegister -> navigateToRegister()
            LoginEvent.ShowError -> showError()
            LoginEvent.NoLongerLoading -> _uiState.value = Loading(false)
        }
    }

    private fun login(username: String, password: String) {
        viewModelScope.launch(dispatcher) {
            try {
                val userAuth = UserAuth(username, password)
                val jwt = loginUseCase.invoke(userAuth)

                when (jwt) {
                    is NetworkResult.Error -> {
                        _uiState.postValue(LoginState.Error(jwt.message))
                    }
                    is NetworkResult.Loading -> {
                        _uiState.postValue(LoginState.Loading(true))
                    }
                    is NetworkResult.Success -> {
                        saveTokenUseCase.invoke(jwt.data)
                        securePreferencesRepository.saveCredentials(username, password)
                        _uiState.postValue(LoginState.Success(userAuth))
                    }
                }
            } catch (e: Exception) {
                _uiState.postValue(LoginState.Error(e.message ?: "Error desconocido"))
            }
        }
    }

    private fun googleLogin(tokenId: String, email: String, googleId: String, username: String) {
        viewModelScope.launch(dispatcher) {
            try {
                val jwt = loginWithGoogleUseCase.invoke(tokenId)

                when (jwt) {
                    is NetworkResult.Error -> {
                        _uiState.postValue(LoginState.Error(jwt.message))
                    }
                    is NetworkResult.Loading -> {
                        _uiState.postValue(LoginState.Loading(true))
                    }
                    is NetworkResult.Success -> {
                        saveTokenUseCase.invoke(jwt.data)
                        _uiState.postValue(LoginState.Success(UserAuth(username, "", email, 0, googleId)))
                    }
                }
            } catch (e: Exception) {
                _uiState.postValue(LoginState.Error(e.message ?: "Error desconocido"))
            }
        }
    }

    private fun navigateToMain() {
        // Logic to navigate to the main screen
    }

    private fun navigateToRegister() {
        //intent to navigate to the registration scree

    }

    private fun showError() {
        _error.value = "An error occurred"
    }

    fun getError(): MutableLiveData<String> {
        return _error
    }
}