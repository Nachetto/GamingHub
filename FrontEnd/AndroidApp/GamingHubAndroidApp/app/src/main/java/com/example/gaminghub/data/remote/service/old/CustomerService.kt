package com.example.gaminghub.data.remote.service.old

import com.example.gaminghub.data.common.Constants
import com.example.gaminghub.data.model.old.CustomerResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface CustomerService {
    @GET(Constants.CUSTOMERS)
    suspend fun getCustomers(): Response<List<CustomerResponse>>
    @DELETE(Constants.CUSTOMERSID)
    suspend fun deleteCustomer(@Path(Constants.ID) id: Int): Response<ResponseBody>
    @GET(Constants.CUSTOMERSID)
    suspend fun getCustomer(@Path(Constants.ID) id: Int ): Response<CustomerResponse>
}