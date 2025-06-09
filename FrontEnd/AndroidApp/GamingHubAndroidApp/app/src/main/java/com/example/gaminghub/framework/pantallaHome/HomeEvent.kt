package com.example.gaminghub.framework.pantallaHome

sealed class HomeEvent {
    class GetPartidas(val username : String) : HomeEvent()
    class GetPartidasFiltradas( val filter: String) : HomeEvent()
}