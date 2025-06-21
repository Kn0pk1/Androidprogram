package com.example.androidprogram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.androidprogram.databinding.FragmentChangePasswordBinding

class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userManager = UserManager(requireContext())

        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnChangePassword.setOnClickListener {
            attemptPasswordChange()
        }
    }

    private fun attemptPasswordChange() {
        val oldPassword = binding.etOldPassword.text.toString()
        val newPassword = binding.etNewPassword.text.toString()
        val confirmNewPassword = binding.etConfirmNewPassword.text.toString()

        val currentUser = userManager.getCurrentUser()

        if (currentUser == null) {
            Toast.makeText(context, "Ошибка: пользователь не найден", Toast.LENGTH_SHORT).show()
            return
        }

        if (oldPassword != currentUser.password) {
            binding.tilOldPassword.error = "Неверный старый пароль"
            return
        } else {
            binding.tilOldPassword.error = null
        }

        if (newPassword.isEmpty() || newPassword.length < Constants.MIN_PASSWORD_LENGTH ||
            !newPassword.matches(Regex(Constants.Regex.PASSWORD_UPPERCASE)) ||
            !newPassword.matches(Regex(Constants.Regex.PASSWORD_DIGIT))) {
            binding.tilNewPassword.error = "Пароль не соответствует требованиям"
            return
        } else {
            binding.tilNewPassword.error = null
        }

        if (newPassword != confirmNewPassword) {
            binding.tilConfirmNewPassword.error = "Пароли не совпадают"
            return
        } else {
            binding.tilConfirmNewPassword.error = null
        }

        val updatedUser = currentUser.copy(password = newPassword)
        if (userManager.updateUser(updatedUser)) {
            Toast.makeText(context, "Пароль успешно изменен", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        } else {
            Toast.makeText(context, "Не удалось изменить пароль", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 