package com.example.gaminghub.data.model

import com.example.gaminghub.domain.modelo.FriendCard

data class AmigoDTO(
    val nombreAmigo: String,
    val estado: String,
    val fechaSolicitud: String,
    val fechaAceptacion: String?
) {
    fun toAmigoTarjeta(): FriendCard {
        return FriendCard(
            friendName = this.nombreAmigo
        )
    }
}