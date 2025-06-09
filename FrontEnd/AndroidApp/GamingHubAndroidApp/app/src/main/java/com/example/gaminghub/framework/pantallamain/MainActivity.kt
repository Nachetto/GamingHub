package com.example.gaminghub.framework.pantallamain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.gaminghub.R
import com.example.gaminghub.databinding.ActivityMainBinding
import com.example.gaminghub.domain.modelo.old.Customer
import com.example.gaminghub.framework.common.ConstantesFramework
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var primeraVez: Boolean = false
    private lateinit var customAdapter: CustomerAdapter
    private val viewModel: MainViewModel by viewModels()
    private val callback by lazy {
        configContextBar()
    }
    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        primeraVez = true
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        obserbarViewModel()
        configAppBar()
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    private fun setupAdapter() {
        customAdapter = CustomerAdapter(this,
            object : CustomerAdapter.CustomerActions {
                override fun onDelete(customer: Customer) =
                    viewModel.handleEvent(MainEvent.DeleteCustomer(customer))

                override fun onStartSelectMode(customer: Customer) {
                    viewModel.handleEvent(MainEvent.StartSelectMode)
                    viewModel.handleEvent(MainEvent.SeleccionaCustomer(customer))
                }

                override fun itemHasClicked(customer: Customer) {
                    viewModel.handleEvent(MainEvent.SeleccionaCustomer(customer))
                }
            })

        binding.rvCustomers.adapter = customAdapter
        val touchHelper = ItemTouchHelper(customAdapter.swipeGesture)
        touchHelper.attachToRecyclerView(binding.rvCustomers)
    }

    //Observa cambios del estado para actualizar la UI
    private fun obserbarViewModel() {
        viewModel.uiState.observe(this) { estado ->
            // si cambia la lista de customers, se actualiza el adapter
            estado.customers.let {
                if (it.isNotEmpty()) {
                    customAdapter.submitList(it)
                }
            }

            // si cambia el modo de seleccion, se actualiza la action bar
            cambiosModoSeleccion(estado)

            // si hay un error, se muestra un toast
            estado.error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cambiosModoSeleccion(estado: MainState) {
        estado.customersSeleccionadas.let { customersSeleccionados ->
            if (customersSeleccionados.isNotEmpty()) {
                customAdapter.setSelectedItems(customersSeleccionados)
                if (customersSeleccionados.size == 1)
                    actionMode?.title = "1 usuario seleccionado"
                else
                    actionMode?.title =
                        "${customersSeleccionados.size} " + ConstantesFramework.SELECTED
            } else {
                customAdapter.resetSelectMode()
                primeraVez = true
                actionMode?.finish()
            }
        }

        estado.selectMode.let { seleccionado ->
            if (seleccionado) {
                if (primeraVez) {
                    customAdapter.startSelectMode()
                    startSupportActionMode(callback)?.let {
                        actionMode = it
                    }

                    primeraVez = false
                } else {
                    customAdapter.startSelectMode()
                }
            } else {
                customAdapter.resetSelectMode()
                primeraVez = true
                actionMode?.finish()
            }
        }
    }

    private fun configContextBar() = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.context_bar, menu)
            binding.topAppBar.visibility = android.view.View.GONE
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.more -> {
                    viewModel.handleEvent(MainEvent.DeleteCustomersSeleccionados())
                    true
                }

                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            viewModel.handleEvent(MainEvent.ResetSelectMode)
            binding.topAppBar.visibility = android.view.View.VISIBLE
            customAdapter.resetSelectMode()
        }
    }

    private fun configAppBar() {
        // el boton de buscar de la top app bar
        val actionSearch = binding.topAppBar.menu.findItem(R.id.search).actionView as SearchView

        actionSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filtro ->
                    viewModel.handleEvent(MainEvent.GetCustomersFiltrados(filtro))
                }
                return true
            }
        })
    }

}