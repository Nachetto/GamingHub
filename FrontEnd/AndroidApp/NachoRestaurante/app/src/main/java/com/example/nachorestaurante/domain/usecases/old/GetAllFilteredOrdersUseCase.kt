package com.example.nachorestaurante.domain.usecases.old

import com.example.nachorestaurante.data.repositorios.OrderRepository
import com.example.nachorestaurante.domain.modelo.Order
import javax.inject.Inject

class GetAllFilteredOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(customerid:Int) :List<Order>{
        return orderRepository.filterOrders(customerid)
    }
}