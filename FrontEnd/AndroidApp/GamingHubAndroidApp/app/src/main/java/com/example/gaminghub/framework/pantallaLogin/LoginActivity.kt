package com.example.gaminghub.framework.pantallaLogin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gaminghub.data.repositorios.SecurePreferencesRepository
import com.example.gaminghub.databinding.LoginBinding
import com.example.gaminghub.domain.modelo.UserAuth
import com.example.gaminghub.framework.pantallaHome.HomeActivity
import com.example.gaminghub.framework.pantallaRegister.RegisterActivity
import com.example.gaminghub.framework.pantallamain.MainActivity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    @Inject
    lateinit var securePreferencesRepository: SecurePreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSavedLogin()
        setUpListeners()
        observeViewModel()
        setUpGoogleSignIn()
    }

    private fun setupSavedLogin() {
        var savedCredentials = securePreferencesRepository.getCredentials()
        if (savedCredentials.first != null && savedCredentials.second != null) {
            binding.etUsername.setText(savedCredentials.first)
            binding.etPassword.setText(savedCredentials.second)
        }
    }

    private fun setUpListeners() {
        // SI SE LLAMA AL INICIAR SESION, SE LLAMA AL EVENT DE LOGIN CON LOS DATOS DE ENTRADA PARA INICIAR SESION
        with (binding) {
            btnLogin.setOnClickListener {
                viewModel.handleEvent(
                    LoginEvent.Login(
                        username = etUsername.text.toString(),
                        password = etPassword.text.toString()
                    )
                )
            }
            btnRegister.setOnClickListener {
                // Aquí puedes manejar la navegación a la pantalla de registro
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // observar el state del viewModel
    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is LoginState.Success -> {
                    viewModel.handleEvent(LoginEvent.NoLongerLoading)
                    navigateToMain(state.authenticatedUser)
                }
                is LoginState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    viewModel.handleEvent(LoginEvent.NoLongerLoading)
                }
                is LoginState.Loading -> {
                    // alterar la visibilidad del spinner
                    binding.spinner.visibility = if (state.isLoading) {
                        android.view.View.VISIBLE
                    } else {
                        android.view.View.GONE
                    }
                }
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpGoogleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken("1046837964429-is3bmf0ddqc535b2jd9lui2taidtihvu.apps.googleusercontent.com")
            .requestEmail()
            .requestId()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        googleSignInLauncher = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
                val idToken = account.idToken
                val email = account.email
                val googleid = account.id
                val displayName = account.displayName

                if (idToken != null && email != null && displayName != null) {
                    viewModel.handleEvent(
                        LoginEvent.ServerLoginWithGoogleDetails(
                            tokenId = idToken,
                            email = email,
                            googleId = googleid.toString(),
                            username = displayName
                        )
                    )
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Google Login Error: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }

        binding.loginWithGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private fun navigateToMain(authenticatedUser: UserAuth) {
        // Aquí puedes manejar la navegación a la pantalla principal
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("username", authenticatedUser.username)
        startActivity(intent)
        finish()
    }

}

