package com.example.gaminghub.domain.usecases.old

import com.example.gaminghub.data.repositorios.old.OrderRepository
import com.example.gaminghub.domain.modelo.old.Order
import javax.inject.Inject

class GetAllFilteredOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(customerid:Int) :List<Order>{
        return orderRepository.filterOrders(customerid)
    }
}