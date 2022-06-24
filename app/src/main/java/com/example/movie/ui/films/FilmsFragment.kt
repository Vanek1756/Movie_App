package com.example.movie.ui.films

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.movie.R
import com.example.movie.databinding.FilmsFragmentBinding
import com.example.movie.util.viewBinding

class FilmsFragment : Fragment(R.layout.films_fragment) {

    private val binding by viewBinding(FilmsFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController =
            (childFragmentManager.findFragmentById(R.id.fcvFilms) as NavHostFragment).navController
        NavigationUI.setupWithNavController(binding.navigation, navController)
    }
}
