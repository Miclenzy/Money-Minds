package com.technolenz.moneyminds.login

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.technolenz.moneyminds.R

class SignUpActivity : AppCompatActivity() {

    private lateinit var pass_create: EditText
    private lateinit var btn_pass_create: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        // Initialize UI elements
        pass_create = findViewById(R.id.create_pass)
        btn_pass_create = findViewById(R.id.btn_create_pass)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // Check if the button was clicked before
        checkBTNclick()

        // Set window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set the OnClickListener for the create password button
        btn_pass_create.setOnClickListener {
            val password = pass_create.text.toString()

            // Save the password in SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("user_password", password)
            editor.putBoolean("isSignUpClicked", true)
            editor.apply() // Or editor.commit()

            // Optionally, you can show a confirmation message
            Toast.makeText(this, "Password saved successfully!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            finish()
        }
    }

    private fun checkBTNclick() {
        val isSignUpClicked = sharedPreferences.getBoolean("isSignUpClicked", false)

        if (isSignUpClicked) {
            Toast.makeText(this, "Welcome to Money Minds.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.d(TAG, "checkBTNclick: First time sign-up")
        }
    }
}
