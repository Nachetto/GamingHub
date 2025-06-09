package com.example.gaminghub.framework.pantallaHome

import com.example.gaminghub.domain.modelo.PartidaCard

data class HomeState (
    val partidas: List<PartidaCard> = emptyList(),
    val error: String? = null
)