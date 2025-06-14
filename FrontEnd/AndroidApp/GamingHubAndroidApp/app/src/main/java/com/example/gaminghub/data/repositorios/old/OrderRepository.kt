package com.example.gaminghub.data.repositorios.old

import com.example.gaminghub.data.common.Constants
import com.example.gaminghub.data.common.NetworkResult
import com.example.gaminghub.domain.modelo.old.Order
import com.example.gaminghub.data.model.old.toOrder
import com.example.gaminghub.data.remote.service.old.OrderService
import javax.inject.Inject

class OrderRepository @Inject constructor(private val orderService: OrderService) {
    suspend private fun getOrders(): NetworkResult<List<Order>> {
        var l: List<Order> = emptyList()
        orderService.getOrders().body()?.let {
            l = it.map { orderResponse ->
                orderResponse.toOrder()
            }
        }
        return NetworkResult.Success(l)
    }

    suspend fun deleteOrder(id: Int): NetworkResult<String> {

        return try {
            val response = orderService.deleteOrder(id)
            if (response.isSuccessful) {
                val responseBodyString = response.body()?.string() ?: Constants.DELETEOK
                NetworkResult.Success(responseBodyString)
            } else {
                val errorBodyString = response.errorBody()?.string() ?: Constants.UNKNOWNERROR
                NetworkResult.Error(errorBodyString)
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: Constants.UNKNOWNERROR)
        }
    }

    suspend fun createOrder(order: Order): NetworkResult<Order> {
        return try {
            val orderResponse = order.toOrderResponse()
            val response = orderService.createOrder(orderResponse)
            if (response.isSuccessful) {
                response.body()?.let {
                    NetworkResult.Success(it.toOrder())
                } ?: NetworkResult.Error(Constants.ORDERNOTRETURNED)
            } else {
                val errorBodyString = response.errorBody()?.string() ?: Constants.UNKNOWNERROR
                NetworkResult.Error(errorBodyString)
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: Constants.UNKNOWNERROR)
        }
    }





    suspend fun filterOrders(customerid: Int): List<Order> {
        var l: List<Order> = getOrders().dataOrNull() ?: emptyList()
        l = l.filter { order ->
            order.customerId == customerid
        }
        return l
    }

}