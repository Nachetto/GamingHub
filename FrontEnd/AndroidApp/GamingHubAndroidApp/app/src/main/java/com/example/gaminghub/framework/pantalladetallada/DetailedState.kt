package com.example.gaminghub.framework.pantalladetallada

import com.example.gaminghub.domain.modelo.old.Customer
import com.example.gaminghub.domain.modelo.old.Order

data class DetailedState (
    val orders: List<Order> = emptyList(),
    val customer: Customer? = null,
    val error: String? = null
)