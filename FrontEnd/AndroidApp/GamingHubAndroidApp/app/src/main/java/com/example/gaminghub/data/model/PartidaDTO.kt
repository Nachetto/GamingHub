package com.example.gaminghub.data.model

import com.example.gaminghub.domain.modelo.PartidaCard

data class PartidaDTO(
    val id: String,
    val anfitrionNombre: String,
    val juegoNombre: String,
    val estado: String,
    val fechaCreacion: String
){
    fun toPartidaTarjeta(): PartidaCard {
        return PartidaCard(
            id = this.id,
            juegoNombre = this.juegoNombre,
            estado = this.estado,
            fechaCreacion = this.fechaCreacion)
    }
}