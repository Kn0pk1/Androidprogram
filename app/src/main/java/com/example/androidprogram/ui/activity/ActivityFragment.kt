package com.example.androidprogram.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidprogram.R
import com.example.androidprogram.databinding.FragmentActivityBinding
import com.example.androidprogram.ui.activity.tabs.MyActivityFragment
import com.example.androidprogram.ui.activity.tabs.UsersActivityFragment
import com.example.androidprogram.ui.activity.tabs.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ActivityFragment : Fragment() {

    private var _binding: FragmentActivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragments = listOf(
            MyActivityFragment(),
            UsersActivityFragment()
        )
        val adapter = ViewPagerAdapter(this, fragments)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.title_my_activity)
                1 -> getString(R.string.title_users_activity)
                else -> ""
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 