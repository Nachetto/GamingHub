package com.example.gaminghub.framework.pantallaRegister

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.gaminghub.data.repositorios.SecurePreferencesRepository
import com.example.gaminghub.databinding.RegisterBinding
import com.example.gaminghub.framework.pantallaHome.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    @Inject
    lateinit var securePreferencesRepository: SecurePreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListeners()
        observeViewModel()
    }

    private fun setUpListeners() {
        with (binding) {

            registerButton.setOnClickListener {
                val username = registerUsernameBox.text.toString()
                val password = registerPasswordBox.text.toString()
                val phone = registerPhoneNumberBox.text.toString().toInt()
                // regex validation for phone number format
                if (phone < 100000000 || phone > 999999999 || username.isEmpty() || password.isEmpty()) {
                    viewModel.handleEvent(RegisterEvent.ShowError("Invalid input"))
                }
                viewModel.handleEvent(
                    RegisterEvent.Register(
                        username = username,
                        phone = phone,
                        password = password
                    )
                )
            }
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is RegisterState.Loading -> {
                    binding.registerButton.isEnabled = !state.isLoading
                    binding.registerSpinner.visibility = if (state.isLoading) android.view.View.VISIBLE else android.view.View.GONE
                }
                is RegisterState.Success -> {
                    Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    intent.putExtra("username",state.username)
                    finish()
                }
                is RegisterState.Error -> {
                    // toast error message
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
                }
            }
    }

}