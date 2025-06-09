package com.example.gaminghub.framework.pantalladetallada

import com.example.gaminghub.domain.modelo.old.Order

sealed class DetailedEvent {
    class DeleteOrder(val pedido: Order) : DetailedEvent()
    class GetOrders(val id :Int) : DetailedEvent()
    class GetCustomer(val id :Int) : DetailedEvent()
    class AddOrder(val tableid: Int) : DetailedEvent()
}