package com.example.nachorestaurante.framework.pantallaLogin

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nachorestaurante.databinding.LoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginBinding
    private lateinit var customAdapter: LoginAdapter
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
        observeViewModel()
    }


    private fun setupAdapter() {
        customAdapter = LoginAdapter(this,
            object : LoginAdapter.LoginActions {
                override fun onLogin(username: String, password: String) {
                    viewModel.handleEvent(LoginEvent.Login(username, password))
                }

                override fun onRegister() {
                    viewModel.handleEvent(LoginEvent.Register)
                }
            })
        binding.rvLogin.adapter = customAdapter
    }


    private fun observeViewModel() {
        TODO("Not yet implemented")
    }
}