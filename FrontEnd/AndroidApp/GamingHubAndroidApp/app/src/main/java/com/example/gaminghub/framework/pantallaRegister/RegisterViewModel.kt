package com.example.gaminghub.framework.pantallaRegister

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gaminghub.data.common.NetworkResult
import com.example.gaminghub.data.repositorios.SecurePreferencesRepository
import com.example.gaminghub.di.IoDispatcher
import com.example.gaminghub.domain.modelo.UserAuth
import com.example.gaminghub.domain.usecases.LoginUseCase
import com.example.gaminghub.domain.usecases.RegisterUseCase
import com.example.gaminghub.domain.usecases.SaveTokenUseCase
import com.example.gaminghub.framework.pantallaRegister.RegisterState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val securePreferencesRepository: SecurePreferencesRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState= MutableLiveData<RegisterState>()
    val uiState: LiveData<RegisterState> get() = _uiState
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun handleEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.Register -> register(event.username, event.phone, event.password)
            RegisterEvent.NoLongerLoading -> _uiState.value = Loading(false)
            is RegisterEvent.ShowError -> showError(event.error)
        }

    }

    private fun register(username: String, phone: Int, password : String) {
        _uiState.value = RegisterState.Loading(true)

        viewModelScope.launch(dispatcher) {
            try {
                val userAuth = UserAuth(username, password,"", phone)
                val result = registerUseCase.invoke(userAuth)

                when (result) {
                    is NetworkResult.Error -> {
                        _uiState.postValue(RegisterState.Error(result.message))
                    }

                    is NetworkResult.Loading -> {
                        _uiState.postValue(RegisterState.Loading(true))
                    }

                    is NetworkResult.Success -> {
                        proceedToLogin(userAuth)
                    }
                }
            } catch (e: Exception) {
                _uiState.postValue(RegisterState.Error(e.message ?: "Error desconocido"))
            }
        }
    }

    private fun proceedToLogin(userAuth: UserAuth) {
        viewModelScope.launch(dispatcher) {
            try {
                val jwt = loginUseCase.invoke(userAuth)

                when (jwt) {
                    is NetworkResult.Error -> {
                        _uiState.postValue(RegisterState.Error(jwt.message))
                    }
                    is NetworkResult.Loading -> {
                        _uiState.postValue(RegisterState.Loading(true))
                    }
                    is NetworkResult.Success -> {
                        saveTokenUseCase.invoke(jwt.data)
                        securePreferencesRepository.saveCredentials(userAuth.username, userAuth.password)
                        _uiState.postValue(RegisterState.Success(userAuth.username))
                    }
                }
            } catch (e: Exception) {
                _uiState.postValue(RegisterState.Error(e.message ?: "Error desconocido"))
            }
        }


    }

    private fun showError(error1: String) {
        _error.value = error1
    }
}