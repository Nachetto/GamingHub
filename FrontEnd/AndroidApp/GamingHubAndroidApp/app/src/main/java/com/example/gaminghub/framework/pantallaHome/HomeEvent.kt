package com.example.gaminghub.framework.pantallaHome

import com.example.gaminghub.domain.modelo.old.Order

sealed class HomeEvent {
    class GetPartidas(val username : String) : HomeEvent()
    class GetPartidasFiltradas( val filter: String) : HomeEvent()
}