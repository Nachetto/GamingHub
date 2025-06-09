package com.example.gaminghub.framework.pantallamain
import com.example.gaminghub.domain.modelo.old.Customer

sealed class MainEvent {
    class DeleteCustomersSeleccionados() : MainEvent()
    class DeleteCustomer(val customer:Customer) : MainEvent()
    class SeleccionaCustomer(val customer: Customer) : MainEvent()
    class GetCustomersFiltrados(val filtro: String) : MainEvent()
    object GetCustomers : MainEvent()
    object StartSelectMode: MainEvent()
    object ResetSelectMode: MainEvent()
}
