package com.example.gaminghub.framework.pantallaHome

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.gaminghub.R
import com.example.gaminghub.databinding.HomeScreenBinding
import com.example.gaminghub.domain.modelo.PartidaCard
import com.example.gaminghub.framework.pantallaProfile.ProfileActivity
import com.example.gaminghub.framework.pantallaSocial.SocialActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeScreenBinding
    private lateinit var homeAdapter: HomeAdapter
    private val viewModel: HomeViewmodel by viewModels()
    private var username : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        configureAppBarAndNavigationBar()
        setupAdapter()
        loadinitialData()

        observeState()
    }


    private fun observeState() {
        viewModel.uiState.observe(this) { state ->


            state.partidas.let { partidas ->
                if (!partidas.isEmpty()) {
                    homeAdapter.submitList(
                    partidas.map { partida ->
                        PartidaCard(
                            id = "Id: " + partida.id,
                            juegoNombre = "Juego: " + partida.juegoNombre,
                            estado = "Estado: " + partida.estado,
                            fechaCreacion = "Fecha: " + partida.fechaCreacion
                        )
                    }
                )
                    binding.rvPartidas.visibility = android.view.View.VISIBLE
                } else {
                    binding.rvPartidas.visibility = android.view.View.GONE
                }
            }

            state.error?.let { error ->
                if (error.isNotEmpty()) {
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }
            }


        }
    }

    private fun setupAdapter() {
        homeAdapter = HomeAdapter(this)
        binding.rvPartidas.adapter = homeAdapter
    }

    private fun loadinitialData() {
        username = intent.getStringExtra("username") ?: ""
        binding.mensajeBienvenida.text = "Bienvenido de vuelta, "+ username
        viewModel.handleEvent(HomeEvent.GetPartidas(username))
    }

    private fun configureAppBarAndNavigationBar() {

        // el boton de buscar de la top app bar
        val actionSearch = binding.topAppBar.menu.findItem(R.id.search).actionView as SearchView
        actionSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filtro ->
                    viewModel.handleEvent(HomeEvent.GetPartidasFiltradas(filtro))
                }
                return true
            }
        })

        //los botones de la menunavigation bar
        binding.bottomNavViewer.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mainActivityNavigation -> {
                    // Ya estamos en la pantalla de inicio, no hacemos nada
                    true
                }
                R.id.socialActivityNavigation -> {
                    navigateToSocialScreeen()
                    true
                }
                R.id.menu_profile -> {
                    navigateToProfileScreen()
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToProfileScreen() {
        //intent al ProfileActivity pasando el username
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
        finish()
    }

    private fun navigateToSocialScreeen() {
        //intent al SocialActivity pasando el username
        val intent = Intent(this, SocialActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
        finish()
    }

}
