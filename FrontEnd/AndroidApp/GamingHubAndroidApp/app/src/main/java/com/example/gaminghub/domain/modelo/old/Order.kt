package com.example.gaminghub.domain.modelo.old

import com.example.gaminghub.data.model.old.OrderResponse
import java.time.LocalDate

data class Order (
    val id: Int,
    val customerId: Int,
    val orderDate: LocalDate,
    val tableId: Int,
) {
    fun toOrderResponse(): OrderResponse {
        return OrderResponse(
            customerId = customerId,
            orderDate = orderDate.toString(),
            orderId = id,
            tableId = tableId
        )
    }
}