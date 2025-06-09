package com.example.gaminghub.domain.usecases.old

import com.example.gaminghub.data.repositorios.old.CustomerRepository
import com.example.gaminghub.domain.modelo.old.Customer
import com.example.gaminghub.data.common.NetworkResult
import javax.inject.Inject

class GetAllCustomersUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Customer>> {
        return customerRepository.getCustomers()
    }
}