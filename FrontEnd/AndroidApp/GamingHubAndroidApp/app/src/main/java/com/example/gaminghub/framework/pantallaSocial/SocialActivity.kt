package com.example.gaminghub.framework.pantallaSocial

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.gaminghub.R
import com.example.gaminghub.databinding.SocialScreenBinding
import com.example.gaminghub.framework.pantallaHome.HomeActivity
import com.example.gaminghub.framework.pantallaProfile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialActivity : AppCompatActivity() {
    private lateinit var binding: SocialScreenBinding
    private lateinit var socialAdapter: SocialAdapter
    private val viewModel: SocialViewmodel by viewModels()
    private var username : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SocialScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureAppBarAndNavigationBar()
        setupAdapter()
        loadInitialData()

        observeState()
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
                    viewModel.handleEvent(SocialEvent.GetAmigosFiltrados(filtro))
                }
                return true
            }
        })

        //los botones de la menunavigation bar
        binding.bottomNavViewer.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mainActivityNavigation -> {
                    navigateToMainScreen()
                    true
                }
                R.id.socialActivityNavigation -> {
                    // Ya estamos en la pantalla de social, no hacemos nada
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

    private fun setupAdapter() {
        socialAdapter = SocialAdapter(this)
        binding.rvAmigos.adapter = socialAdapter
    }

    private fun loadInitialData() {
        username = intent.getStringExtra("username") ?: ""
        binding.mensajeBienvenida.text = username+ "! mira, estos son tus amigos"
        viewModel.handleEvent(SocialEvent.GetAmigos(username))
    }

    private fun observeState() {
        viewModel.uiState.observe(this) { state ->
            state.amigos?.let { amigos ->
                if (amigos.isNotEmpty()) {
                    socialAdapter.submitList(amigos.map {
                        amigo ->
                        amigo.copy( friendName = amigo.friendName ?: "Desconocido")
                    })
                    binding.rvAmigos.visibility = android.view.View.VISIBLE
                } else {
                    binding.rvAmigos.visibility = android.view.View.GONE
                }
            }

            state.error?.let { error ->
                if (error.isNotEmpty()) {
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToProfileScreen() {
//         Intent al ProfileActivity pasando el username
         val intent = Intent(this, ProfileActivity::class.java)
         intent.putExtra("username", username)
         startActivity(intent)
         finish()
    }

    private fun navigateToMainScreen() {
        //Intent al HomeActivity pasando el username
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
        finish()
    }
}