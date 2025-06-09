package com.example.gaminghub.domain.usecases.old

import com.example.gaminghub.data.repositorios.old.CustomerRepository
import com.example.gaminghub.domain.modelo.old.Customer
import java.time.LocalDate
import javax.inject.Inject

class GetCustomerFromIdUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke(customerid: Int): Customer {
        return customerRepository.getCustomerFromId(customerid).dataOrNull()?:
            Customer(0,"","","",0,LocalDate.of(0,0,0),false)
    }
}