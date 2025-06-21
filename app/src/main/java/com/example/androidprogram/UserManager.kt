package com.example.androidprogram

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserManager(private val context: Context) {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "user_prefs", Context.MODE_PRIVATE
    )
    private val gson = Gson()
    
    companion object {
        private const val KEY_USERS = "users"
        private const val KEY_CURRENT_USER = "current_user"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
    
    // Сохранение нового пользователя
    fun registerUser(user: User): Boolean {
        val users = getAllUsers().toMutableList()
        
        // Проверяем, не существует ли уже пользователь с таким логином
        if (users.any { it.login == user.login }) {
            return false
        }
        
        users.add(user)
        saveUsers(users)
        return true
    }
    
    // Получение всех пользователей
    private fun getAllUsers(): List<User> {
        val usersJson = sharedPreferences.getString(KEY_USERS, "[]")
        val type = object : TypeToken<List<User>>() {}.type
        return try {
            gson.fromJson(usersJson, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Сохранение списка пользователей
    private fun saveUsers(users: List<User>) {
        val usersJson = gson.toJson(users)
        sharedPreferences.edit().putString(KEY_USERS, usersJson).apply()
    }
    
    // Обновление данных пользователя
    fun updateUser(updatedUser: User): Boolean {
        val users = getAllUsers().toMutableList()
        val userIndex = users.indexOfFirst { it.login == updatedUser.login }

        if (userIndex != -1) {
            users[userIndex] = updatedUser
            saveUsers(users)
            // Также обновляем текущего пользователя, если редактируется он
            if (getCurrentUser()?.login == updatedUser.login) {
                setCurrentUser(updatedUser)
            }
            return true
        }
        return false
    }
    
    // Авторизация пользователя
    fun loginUser(login: String, password: String): User? {
        val users = getAllUsers()
        return users.find { it.login == login && it.password == password }
    }
    
    // Сохранение текущего пользователя
    fun setCurrentUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit()
            .putString(KEY_CURRENT_USER, userJson)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }
    
    // Получение текущего пользователя
    fun getCurrentUser(): User? {
        if (!isLoggedIn()) return null
        
        val userJson = sharedPreferences.getString(KEY_CURRENT_USER, null)
        return try {
            gson.fromJson(userJson, User::class.java)
        } catch (e: Exception) {
            null
        }
    }
    
    // Проверка авторизации
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    // Выход из аккаунта
    fun logout() {
        sharedPreferences.edit()
            .remove(KEY_CURRENT_USER)
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .apply()
    }
    
    // Проверка существования пользователя
    fun isUserExists(login: String): Boolean {
        val users = getAllUsers()
        return users.any { it.login == login }
    }
} 