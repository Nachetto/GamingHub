package com.example.gaminghub.framework.pantallaProfile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.gaminghub.R
import com.example.gaminghub.databinding.ProfileScreenBinding
import com.example.gaminghub.framework.pantallaHome.HomeActivity
import com.example.gaminghub.framework.pantallaHome.HomeEvent
import com.example.gaminghub.framework.pantallaSocial.SocialActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {


    private lateinit var binding: ProfileScreenBinding
    private var username : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        configureAppBarAndNavigationBar()
        loadinitialData()
    }


    private fun loadinitialData() {
        val randomPhone = (1000000000..9999999999).random().toString() // Genera un número de teléfono aleatorio
        val randomDate = "${(1..28).random()}/${(1..12).random()}/${(2000..2023).random()}" // Genera una fecha aleatoria

        username = "Username: " + intent.getStringExtra("username").orEmpty()
        binding.tvUsername.text = username
        binding.tvPhone.text = "Teléfono: $randomPhone"
        binding.tvRegistrationDate.text = "Fecha de Registro: $randomDate"
    }

    private fun configureAppBarAndNavigationBar() {

        // el boton de buscar de la top app bar
        val actionSearch = binding.topAppBar.menu.findItem(R.id.search).actionView as SearchView
        actionSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
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
                    navigateToSocialScreeen()
                    true
                }
                R.id.menu_profile -> {
                    // Ya estamos en la pantalla de inicio, no hacemos nada
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToSocialScreeen() {
        //intent al SocialActivity pasando el username
        val intent = Intent(this, SocialActivity::class.java)
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