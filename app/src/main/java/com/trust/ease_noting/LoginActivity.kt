package com.trust.ease_noting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    private lateinit var loginbutton:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginbutton = findViewById(R.id.buttonLogin)

        loginbutton.setOnClickListener{
            val myintent = Intent(this@LoginActivity,Homepage::class.java)
            startActivity(myintent)
        }
    }
}