package com.technolenz.moneyminds.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.technolenz.moneyminds.AppClearActivity
import com.technolenz.moneyminds.MainActivity
import com.technolenz.moneyminds.R

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var retrievedPassword: String
    private lateinit var btn_login: Button
    private lateinit var pass_txt: EditText
    private lateinit var forgot_pass_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        pass_txt = findViewById(R.id.loginpasstxt)
        btn_login = findViewById(R.id.btn_login)
        forgot_pass_btn = findViewById(R.id.forgot_pass)

        // Initialize SharedPreferences and retrieve the saved password
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        retrievedPassword = sharedPreferences.getString("user_password", null) ?: ""

        // Set the onClickListener for the login button
        btn_login.setOnClickListener {
            LogFunc()
        }

        // Set the onClickListener for the forgot password button
        forgot_pass_btn.setOnClickListener {
            val i = Intent(this, AppClearActivity::class.java)
            startActivity(i)
            finish()
        }

        // Handle window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun LogFunc() {
        // Retrieve the text input from the EditText
        val passwordInput = pass_txt.text.toString()

        // Compare the retrieved password with the input
        if (retrievedPassword == passwordInput) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Show a message if the password is incorrect
            Toast.makeText(this, "Wrong Password, Try Again", Toast.LENGTH_SHORT).show()
        }
    }
}
