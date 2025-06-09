package com.example.gaminghub.data.model.old

import com.example.gaminghub.data.common.Constants
import com.example.gaminghub.domain.modelo.old.Order
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class OrderResponse(
    @SerializedName(Constants.CUSTOMERID)
    val customerId: Int,

    @SerializedName(Constants.ORDERDATE)
    val orderDate: String,

    @SerializedName(Constants.ID)
    val orderId: Int,

    @SerializedName(Constants.TABLEID)
    val tableId: Int
)
fun OrderResponse.toOrder(): Order {
    return Order(
        customerId = customerId,
        orderDate = LocalDate.parse(orderDate),
        id = orderId,
        tableId = tableId
    )
}