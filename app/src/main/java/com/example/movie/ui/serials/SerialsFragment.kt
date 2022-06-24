package com.example.movie.ui.serials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.movie.R
import com.example.movie.databinding.SerialsFragmentBinding
import com.example.movie.util.viewBinding

class SerialsFragment : Fragment(R.layout.serials_fragment) {
    private val binding by viewBinding(SerialsFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController =
            (childFragmentManager.findFragmentById(R.id.fcvSerials) as NavHostFragment).navController
        NavigationUI.setupWithNavController(binding.navigation, navController)
    }
}
