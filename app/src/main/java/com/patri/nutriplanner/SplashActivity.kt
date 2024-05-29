package com.patri.nutriplanner

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.patri.nutriplanner.User.UserActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Mostrar el splash screen por 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            val nextActivity = MainActivity::class.java
            startActivity(Intent(this, nextActivity))
            finish()
        }, 3000) // 3000 milisegundos = 3 segundos
    }
}
