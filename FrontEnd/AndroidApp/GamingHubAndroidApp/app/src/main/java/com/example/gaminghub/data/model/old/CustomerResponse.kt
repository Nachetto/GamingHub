package com.example.gaminghub.data.model.old

import com.example.gaminghub.data.common.Constants
import com.example.gaminghub.domain.modelo.old.Customer
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class CustomerResponse(
    @SerializedName(Constants.ID)
    val id: Int,

    @SerializedName(Constants.NAME)
    val name: String,

    @SerializedName(Constants.SURNAME)
    val surname: String,

    @SerializedName(Constants.EMAIL)
    val email: String,

    @SerializedName(Constants.PHONE)
    val phone: Int,

    @SerializedName(Constants.BIRTHDATE)
    val birthdate: String
)

fun CustomerResponse.toCustomer(): Customer {
    return Customer(
        id = id,
        name = name,
        surname = surname,
        email = email,
        phone = phone,
        birthdate = LocalDate.parse(birthdate)
    )
}
