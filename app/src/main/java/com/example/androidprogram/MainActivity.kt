package com.example.androidprogram

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val userManager = UserManager(this)
        
        // Проверяем, авторизован ли пользователь
        if (userManager.isLoggedIn()) {
            // Если пользователь уже авторизован, переходим к главному экрану
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {
            // Если не авторизован, переходим к приветственному экрану
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }
        
        finish()
    }
}