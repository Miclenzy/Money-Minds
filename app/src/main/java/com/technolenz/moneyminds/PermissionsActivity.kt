package com.technolenz.moneyminds

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.technolenz.moneyminds.login.SignUpActivity

class PermissionsActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_PERMISSIONS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_PERMISSIONS
            )
        } else {
            // Permissions already granted
            navigateToMainActivity()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All permissions granted
                navigateToMainActivity()
            } else {
                // Permissions denied
                Log.e("PermissionsActivity", "Permissions not granted!")
                // Handle permissions denial
            }
        }
    }

    private fun navigateToMainActivity() {
        // Navigate to your main activity or wherever you need
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        // Close Permmisions activity
        finish()
    }
}
