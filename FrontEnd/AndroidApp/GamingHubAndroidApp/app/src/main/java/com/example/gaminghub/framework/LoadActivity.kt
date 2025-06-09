package com.example.gaminghub.framework

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.gaminghub.framework.pantallaLogin.LoginActivity

class LoadActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 500)

    }
}