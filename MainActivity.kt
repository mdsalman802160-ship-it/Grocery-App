package com.example.groceryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etMobile = findViewById<EditText>(R.id.etMobile)
        val etOtp = findViewById<EditText>(R.id.etOtp)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val mobile = etMobile.text.toString().trim()
            val otp = etOtp.text.toString().trim()

            if (mobile.length == 10 && otp == "1234") {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login Fail! OTP: 1234", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
