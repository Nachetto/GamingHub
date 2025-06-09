package com.example.gaminghub.framework.pantallaSocial

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gaminghub.data.common.NetworkResult
import com.example.gaminghub.domain.modelo.FriendCard
import com.example.gaminghub.domain.usecases.GetAmigosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SocialViewmodel @Inject constructor(
    private val getAmigosUseCase: GetAmigosUseCase,
) : ViewModel() {
    private val listaAmigos = mutableListOf<FriendCard>()
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error
    private val _uiState = MutableLiveData(SocialState())
    val uiState: LiveData<SocialState> get() = _uiState

    init {
        _uiState.value = SocialState(
            amigos = emptyList(),
            error = this.error.value
        )
    }

    fun handleEvent(event: SocialEvent) {
        when (event) {
            is SocialEvent.GetAmigos -> {
                getAmigos(event.username)
            }

            is SocialEvent.GetAmigosFiltrados -> filterAmigos(event.filter)
        }
    }

    private fun filterAmigos(filtro: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(
                amigos = listaAmigos.filter { amigo ->
                    amigo.friendName.contains(filtro, ignoreCase = true)
                }.toList()
            )
        }

    }

    private fun getAmigos(username: String) {


        viewModelScope.launch {
            try {
                val result = getAmigosUseCase.invoke(username)
                when (result) {
                    is NetworkResult.Success -> {
                        listaAmigos.clear()
                        result.data?.let { amigos ->
                            listaAmigos.addAll(amigos.map { amigoDTO ->
                                amigoDTO.toAmigoTarjeta()
                            })
                        }
                        _uiState.value = _uiState.value?.copy(
                            amigos = listaAmigos
                        )
                    }

                    is NetworkResult.Error -> {
                        _error.value = result.message ?: "Unknown error"
                    }

                    is NetworkResult.Loading<*> -> Toast.makeText(
                        null,
                        "Loading...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }

    }

}