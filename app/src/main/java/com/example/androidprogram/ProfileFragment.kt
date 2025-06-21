package com.example.androidprogram

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.androidprogram.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userManager: UserManager
    private var currentUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userManager = UserManager(requireContext())
        setupUserInfo()
        setupClickListeners()
    }

    private fun setupUserInfo() {
        currentUser = userManager.getCurrentUser()

        currentUser?.let { user ->
            binding.etLogin.setText(user.login)
            binding.etName.setText(user.name)
        }
    }

    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener {
            saveUserProfile()
        }

        binding.tvChangePassword.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, ChangePasswordFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnLogout.setOnClickListener {
            userManager.logout()
            Toast.makeText(requireContext(), getString(R.string.logout_success), Toast.LENGTH_SHORT).show()

            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun saveUserProfile() {
        val newName = binding.etName.text.toString().trim()

        if (newName.isEmpty()) {
            binding.tilName.error = Constants.ErrorMessages.NAME_EMPTY
            return
        }

        if (newName.length < Constants.MIN_NAME_LENGTH) {
            binding.tilName.error = Constants.ErrorMessages.NAME_TOO_SHORT
            return
        } else {
            binding.tilName.error = null
        }

        currentUser?.let {
            val updatedUser = it.copy(name = newName)
            if (userManager.updateUser(updatedUser)) {
                Toast.makeText(requireContext(), "Профиль успешно обновлен", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Не удалось обновить профиль", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Обновляем информацию о пользователе, если она могла измениться (например, пароль)
        setupUserInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 