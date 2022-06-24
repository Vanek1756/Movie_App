package com.example.movie.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.movie.R
import com.example.movie.databinding.ActivityMainBinding
import com.example.movie.ui.FilterDialogFragment
import com.example.movie.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            val navController =
                (supportFragmentManager.findFragmentById(R.id.fcvMain) as NavHostFragment).navController
            NavigationUI.setupWithNavController(wrapper.navigation, navController)

            setSupportActionBar(wrapper.customToolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            wrapper.customToolbar.title = "Movie App"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.overflow_action_filter -> {
                FilterDialogFragment().show(supportFragmentManager, "FilterDialogFragment")
            }
            R.id.overflow_action_search -> {
                val controller = Navigation.findNavController(this, R.id.fcvMain)
                controller.navigate(R.id.searchFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
