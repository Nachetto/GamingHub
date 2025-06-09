package com.example.gaminghub.domain.usecases.old

import com.example.gaminghub.data.common.NetworkResult
import com.example.gaminghub.data.repositorios.old.OrderRepository
import com.example.gaminghub.domain.modelo.old.Order
import javax.inject.Inject

class AddOrderUseCase @Inject constructor(private val repository: OrderRepository) {
    suspend operator fun invoke(order: Order): NetworkResult<Order> {
        return repository.createOrder(order)
    }
}