package com.example.gaminghub.framework.pantallaSocial

import com.example.gaminghub.domain.modelo.FriendCard

data class SocialState (
    val amigos: List<FriendCard> = emptyList(),
    val error: String? = null
)