package com.example.gaminghub.framework.pantallamain

import com.example.gaminghub.domain.modelo.old.Customer

data class MainState (
    val customers: List<Customer> = emptyList(),
    val customersSeleccionadas: List<Customer> = emptyList(),
    val selectMode: Boolean = false,
    val error: String? = null
)