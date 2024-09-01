package com.technolenz.moneyminds

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import java.io.File

class AppClearActivity : AppCompatActivity() {

    private lateinit var btnExportAndReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_clear)

        btnExportAndReset = findViewById(R.id.btn_export_and_reset)

        btnExportAndReset.setOnClickListener {
            if (hasWritePermission()) {
                exportDataAndResetApp()
            } else {
                requestWritePermission()
            }
        }
    }

    private fun hasWritePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestWritePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_WRITE_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_WRITE_STORAGE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            exportDataAndResetApp()
        } else {
            Toast.makeText(this, "Permission denied to write to storage.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun exportDataAndResetApp() {
        exportAppData()
        resetApp()
    }

    private fun exportAppData() {
        val exportDir = File(getExternalFilesDir(null), "AppBackup")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        // Export SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val prefsFile = File(exportDir, "shared_prefs.json")
        val prefsJson = Gson().toJson(sharedPreferences.all)
        prefsFile.writeText(prefsJson)

        // Export Files (if any)
        val filesDir = filesDir
        filesDir.listFiles()?.forEach { file ->
            val exportFile = File(exportDir, file.name)
            file.copyTo(exportFile, overwrite = true)
        }

        Toast.makeText(this, "Data exported to ${exportDir.absolutePath}", Toast.LENGTH_LONG).show()
    }

    private fun resetApp() {
        // Clear SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        // Clear Databases
        deleteDatabase("your_database_name")

        // Clear Files
        filesDir.deleteRecursively()

        // Restart the app or navigate to the initial setup screen
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val REQUEST_WRITE_STORAGE = 1
    }
}
