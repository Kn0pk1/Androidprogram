package com.example.androidprogram

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.androidprogram.databinding.ActivityHomeBinding
import com.example.androidprogram.ui.activity.ActivityFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val activityFragment = ActivityFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.nav_host_fragment, activityFragment, "activity")
                .commit()
        }

        // Явно устанавливаем вкладку "Активность" как выбранную
        binding.bottomNavigationView.selectedItemId = R.id.navigation_activity

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, NewActivity::class.java)
            startActivity(intent)
        }

        setCurrentFragment(activityFragment, fab)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_activity -> {
                    switchFragment(activityFragment, "activity")
                    setCurrentFragment(activityFragment, fab)
                    true
                }
                R.id.navigation_profile -> {
                    switchFragment(profileFragment, "profile")
                    setCurrentFragment(profileFragment, fab)
                    true
                }
                else -> false
            }
        }
    }

    override fun onBackPressed() {
        if (binding.bottomNavigationView.selectedItemId == R.id.navigation_profile) {
            binding.bottomNavigationView.selectedItemId = R.id.navigation_activity
        } else {
            super.onBackPressed()
        }
    }

    private fun switchFragment(fragment: Fragment, tag: String) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val newFragment = supportFragmentManager.findFragmentByTag(tag)

        val transaction = supportFragmentManager.beginTransaction()

        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }

        if (newFragment == null) {
            transaction.add(R.id.nav_host_fragment, fragment, tag)
        } else {
            transaction.show(newFragment)
        }
        transaction.commit()
    }

    private fun setCurrentFragment(fragment: Fragment, fab: FloatingActionButton) {
        if (fragment is ProfileFragment) {
            fab.visibility = View.GONE
        } else {
            fab.visibility = View.VISIBLE
        }
    }
} 