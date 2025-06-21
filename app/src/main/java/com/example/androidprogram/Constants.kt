package com.example.androidprogram

object Constants {
    // Минимальная длина логина
    const val MIN_LOGIN_LENGTH = 3
    
    // Минимальная длина имени
    const val MIN_NAME_LENGTH = 2
    
    // Минимальная длина пароля
    const val MIN_PASSWORD_LENGTH = 6
    
    // Регулярные выражения для валидации
    object Regex {
        const val LOGIN_PATTERN = "^[a-zA-Z0-9_]+$"
        const val PASSWORD_UPPERCASE = ".*[A-Z].*"
        const val PASSWORD_DIGIT = ".*[0-9].*"
    }
    
    // Сообщения об ошибках
    object ErrorMessages {
        const val LOGIN_EMPTY = "Введите логин"
        const val LOGIN_TOO_SHORT = "Логин должен содержать минимум $MIN_LOGIN_LENGTH символа"
        const val LOGIN_INVALID_CHARS = "Логин может содержать только буквы, цифры и знак подчеркивания"
        const val LOGIN_EXISTS = "Пользователь с таким логином уже существует"
        
        const val NAME_EMPTY = "Введите имя или никнейм"
        const val NAME_TOO_SHORT = "Имя должно содержать минимум $MIN_NAME_LENGTH символа"
        
        const val PASSWORD_EMPTY = "Введите пароль"
        const val PASSWORD_TOO_SHORT = "Пароль должен содержать минимум $MIN_PASSWORD_LENGTH символов"
        const val PASSWORD_NO_UPPERCASE = "Пароль должен содержать хотя бы одну заглавную букву"
        const val PASSWORD_NO_DIGIT = "Пароль должен содержать хотя бы одну цифру"
        
        const val CONFIRM_PASSWORD_EMPTY = "Подтвердите пароль"
        const val PASSWORDS_DONT_MATCH = "Пароли не совпадают"
        
        const val GENDER_NOT_SELECTED = "Выберите пол"
    }
} 