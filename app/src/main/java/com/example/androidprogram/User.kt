package com.example.androidprogram

data class User(
    val login: String,
    val name: String,
    val password: String,
    val gender: String,
    val registrationDate: Long = System.currentTimeMillis()
) {
    companion object {
        const val GENDER_MALE = "male"
        const val GENDER_FEMALE = "female"
        const val GENDER_OTHER = "other"
    }
} 