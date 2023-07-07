package com.example.educationplatform.presentation.bottomMenu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.BottomNavFragmentBinding

class BottomMenuFragment: Fragment(R.layout.bottom_nav_fragment) {
    private val binding by viewBinding<BottomNavFragmentBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = (childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
        binding.bottomMenu.setupWithNavController(navController)
    }
}