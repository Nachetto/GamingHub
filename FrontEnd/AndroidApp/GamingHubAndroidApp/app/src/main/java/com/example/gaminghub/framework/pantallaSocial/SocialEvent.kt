package com.example.gaminghub.framework.pantallaSocial


sealed class SocialEvent {
    class GetAmigos(val username : String) : SocialEvent()
    class GetAmigosFiltrados( val filter: String) : SocialEvent()
}