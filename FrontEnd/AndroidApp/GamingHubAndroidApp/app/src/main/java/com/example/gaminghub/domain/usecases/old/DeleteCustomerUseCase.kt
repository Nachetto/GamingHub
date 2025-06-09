package com.example.gaminghub.domain.usecases.old

import com.example.gaminghub.data.common.NetworkResult
import com.example.gaminghub.data.repositorios.old.CustomerRepository
import com.example.gaminghub.domain.modelo.old.Customer
import javax.inject.Inject

class DeleteCustomerUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
)   {
    suspend operator fun invoke(customer: Customer):NetworkResult<String> {
        return customerRepository.deleteCustomer(customer.id)
    }
}