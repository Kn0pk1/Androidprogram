package com.example.androidprogram

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.androidprogram.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userManager: UserManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        userManager = UserManager(this)
        
        setupToolbar()
        setupButtonClicks()
        setupTextWatchers()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
        binding.toolbar.setTitle(R.string.login_title)

        val upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        upArrow?.setTint(ContextCompat.getColor(this, R.color.primary_color))
        supportActionBar?.setHomeAsUpIndicator(upArrow)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun setupButtonClicks() {
        binding.btnLogin.setOnClickListener {
            if (validateForm()) {
                performLogin()
            }
        }
    }
    
    private fun setupTextWatchers() {
        // Очищаем ошибки при вводе
        binding.etLogin.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                binding.tilLogin.error = null
            }
        })
        
        binding.etPassword.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                binding.tilPassword.error = null
            }
        })
    }
    
    private fun performLogin() {
        val login = binding.etLogin.text.toString().trim()
        val password = binding.etPassword.text.toString()
        
        val user = userManager.loginUser(login, password)
        
        if (user != null) {
            userManager.setCurrentUser(user)
            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
            
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.invalid_credentials), Toast.LENGTH_LONG).show()
        }
    }
    
    private fun validateForm(): Boolean {
        val login = binding.etLogin.text.toString().trim()
        val password = binding.etPassword.text.toString()
        
        if (login.isEmpty()) {
            binding.tilLogin.error = Constants.ErrorMessages.LOGIN_EMPTY
            return false
        }
        
        if (password.isEmpty()) {
            binding.tilPassword.error = Constants.ErrorMessages.PASSWORD_EMPTY
            return false
        }
        
        return true
    }
} 