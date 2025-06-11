package com.example.gaminghub.framework.pantallaHome

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gaminghub.data.common.NetworkResult
import com.example.gaminghub.data.model.PartidaDTO
import com.example.gaminghub.domain.modelo.PartidaCard
import com.example.gaminghub.domain.usecases.GetPartidasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewmodel @Inject constructor(
     private val getPartidasUseCase: GetPartidasUseCase,
) : ViewModel() {
    private val listaPartidas = mutableListOf<PartidaCard>()
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error
    private val _uiState = MutableLiveData(HomeState())
    val uiState: LiveData<HomeState> get() = _uiState

    init {
        _uiState.value = HomeState(
            partidas = emptyList(),
            error = this.error.value
        )
    }

    fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.GetPartidas -> {
                getPartidas(event.username)
            }

            is HomeEvent.GetPartidasFiltradas -> filterPartidas(event.filter)
        }
    }


    private fun filterPartidas(filtro: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(
                partidas = listaPartidas.filter { partida ->
                    partida.juegoNombre.contains(filtro, ignoreCase = true)
                }.toList()
            )
        }
    }

    private fun getPartidas(username: String) {
        viewModelScope.launch {
            try {
                val result = getPartidasUseCase.invoke(username)
                when (result) {
                    is NetworkResult.Success -> {
                        listaPartidas.clear()
                        result.data?.let { partidas ->
                            listaPartidas.addAll(partidas.map { partidaDTO ->
                                partidaDTO.toPartidaTarjeta()
                            })
                        }
                        _uiState.value = _uiState.value?.copy(
                            partidas = listaPartidas
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