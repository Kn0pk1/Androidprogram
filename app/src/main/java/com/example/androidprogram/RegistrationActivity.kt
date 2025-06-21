package com.example.androidprogram

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.androidprogram.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var userManager: UserManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        userManager = UserManager(this)
        
        setupToolbar()
        setupClickableText()
        setupButtonClicks()
        setupTextWatchers()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
        binding.toolbar.setTitle(R.string.registration_title)

        val upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        upArrow?.setTint(ContextCompat.getColor(this, R.color.primary_color))
        supportActionBar?.setHomeAsUpIndicator(upArrow)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun setupClickableText() {
        val fullText = getString(R.string.privacy_policy_text)
        val privacyPolicy = getString(R.string.privacy_policy)
        val userAgreement = getString(R.string.user_agreement)

        val spannableString = SpannableString(fullText)

        val privacyPolicyClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(this@RegistrationActivity, "Политика конфиденциальности", Toast.LENGTH_SHORT).show()
            }
        }

        val userAgreementClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(this@RegistrationActivity, "Пользовательское соглашение", Toast.LENGTH_SHORT).show()
            }
            override fun updateDrawState(ds: android.text.TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }

        val privacyPolicyStartIndex = fullText.indexOf(privacyPolicy)
        if (privacyPolicyStartIndex != -1) {
            spannableString.setSpan(
                privacyPolicyClickableSpan,
                privacyPolicyStartIndex,
                privacyPolicyStartIndex + privacyPolicy.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        val userAgreementStartIndex = fullText.indexOf(userAgreement)
        if (userAgreementStartIndex != -1) {
            spannableString.setSpan(
                userAgreementClickableSpan,
                userAgreementStartIndex,
                userAgreementStartIndex + userAgreement.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        binding.tvPrivacyPolicy.text = spannableString
        binding.tvPrivacyPolicy.movementMethod = LinkMovementMethod.getInstance()
        binding.tvPrivacyPolicy.highlightColor = android.graphics.Color.TRANSPARENT
    }
    
    private fun setupButtonClicks() {
        binding.btnRegister.setOnClickListener {
            if (validateForm()) {
                performRegistration()
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
        
        binding.etName.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                binding.tilName.error = null
            }
        })
        
        binding.etPassword.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                binding.tilPassword.error = null
            }
        })
        
        binding.etConfirmPassword.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                binding.tilConfirmPassword.error = null
            }
        })
    }
    
    private fun performRegistration() {
        val login = binding.etLogin.text.toString().trim()
        val name = binding.etName.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val gender = getSelectedGender()
        
        val user = User(
            login = login,
            name = name,
            password = password,
            gender = gender
        )
        
        if (userManager.registerUser(user)) {
            // Автоматически входим в систему после регистрации
            userManager.setCurrentUser(user)
            
            Toast.makeText(this, getString(R.string.registration_success), Toast.LENGTH_SHORT).show()
            
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.user_exists_error), Toast.LENGTH_LONG).show()
        }
    }
    
    private fun getSelectedGender(): String {
        return when (binding.rgGender.checkedRadioButtonId) {
            R.id.rb_male -> User.GENDER_MALE
            R.id.rb_female -> User.GENDER_FEMALE
            R.id.rb_other -> User.GENDER_OTHER
            else -> User.GENDER_MALE
        }
    }
    
    private fun validateForm(): Boolean {
        val login = binding.etLogin.text.toString().trim()
        val name = binding.etName.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        
        // Валидация логина
        if (login.isEmpty()) {
            binding.tilLogin.error = Constants.ErrorMessages.LOGIN_EMPTY
            return false
        }
        
        if (login.length < Constants.MIN_LOGIN_LENGTH) {
            binding.tilLogin.error = Constants.ErrorMessages.LOGIN_TOO_SHORT
            return false
        }
        
        if (!login.matches(Regex(Constants.Regex.LOGIN_PATTERN))) {
            binding.tilLogin.error = Constants.ErrorMessages.LOGIN_INVALID_CHARS
            return false
        }
        
        // Проверка существования пользователя
        if (userManager.isUserExists(login)) {
            binding.tilLogin.error = Constants.ErrorMessages.LOGIN_EXISTS
            return false
        }
        
        // Валидация имени
        if (name.isEmpty()) {
            binding.tilName.error = Constants.ErrorMessages.NAME_EMPTY
            return false
        }
        
        if (name.length < Constants.MIN_NAME_LENGTH) {
            binding.tilName.error = Constants.ErrorMessages.NAME_TOO_SHORT
            return false
        }
        
        // Валидация пароля
        if (password.isEmpty()) {
            binding.tilPassword.error = Constants.ErrorMessages.PASSWORD_EMPTY
            return false
        }
        
        if (password.length < Constants.MIN_PASSWORD_LENGTH) {
            binding.tilPassword.error = Constants.ErrorMessages.PASSWORD_TOO_SHORT
            return false
        }
        
        if (!password.matches(Regex(Constants.Regex.PASSWORD_UPPERCASE))) {
            binding.tilPassword.error = Constants.ErrorMessages.PASSWORD_NO_UPPERCASE
            return false
        }
        
        if (!password.matches(Regex(Constants.Regex.PASSWORD_DIGIT))) {
            binding.tilPassword.error = Constants.ErrorMessages.PASSWORD_NO_DIGIT
            return false
        }
        
        // Валидация подтверждения пароля
        if (confirmPassword.isEmpty()) {
            binding.tilConfirmPassword.error = Constants.ErrorMessages.CONFIRM_PASSWORD_EMPTY
            return false
        }
        
        if (confirmPassword != password) {
            binding.tilConfirmPassword.error = Constants.ErrorMessages.PASSWORDS_DONT_MATCH
            return false
        }
        
        // Валидация выбора пола
        if (binding.rgGender.checkedRadioButtonId == -1) {
            Toast.makeText(this, Constants.ErrorMessages.GENDER_NOT_SELECTED, Toast.LENGTH_SHORT).show()
            return false
        }
        
        return true
    }
} 