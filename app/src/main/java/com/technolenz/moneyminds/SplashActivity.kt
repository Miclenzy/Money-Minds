package com.technolenz.moneyminds

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Duration of the splash screen
        val splashScreenDuration = 3000L // 3000 milliseconds = 3 seconds

        // Handler to start the MainActivity after the splash screen duration
        Handler(Looper.getMainLooper()).postDelayed({
            // Start MainActivity
            val intent = Intent(this, PermissionsActivity::class.java)
            startActivity(intent)
            // Close SplashActivity
            finish()
        }, splashScreenDuration)
    }
}
