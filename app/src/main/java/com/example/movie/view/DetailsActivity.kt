package com.example.movie.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.movie.R
import com.example.movie.databinding.DetailsActivityBinding
import com.example.movie.ui.details.MovieDetailsFragmentDirections
import com.example.movie.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    companion object {
        const val MOVIE_DATA_EXTRA = "movieData"
        const val SERIALS_DATA_EXTRA = "serialsData"
        const val PEOPLE_DATA_EXTRA = "peopleData"
    }

    private val binding by viewBinding(DetailsActivityBinding::inflate)

    private var filmId: Int? = null
    private var serialId: Int? = null
    private var peopleName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fcvDetails
        ) as NavHostFragment
        val navController = navHostFragment.navController

        val intent = intent
        if (intent != null && intent.hasExtra(MOVIE_DATA_EXTRA)) {
            filmId = intent.getIntExtra(MOVIE_DATA_EXTRA, 0)
            filmId?.let {
                val action =
                    MovieDetailsFragmentDirections.actionMovieDetailsFragmentToMovieDetailsFragment(
                        it
                    )
                navController.navigate(action)
            }
        }
        if (intent != null && intent.hasExtra(SERIALS_DATA_EXTRA)) {
            serialId = intent.getIntExtra(SERIALS_DATA_EXTRA, 0)
            serialId?.let {
                val action =
                    MovieDetailsFragmentDirections.actionMovieDetailsFragmentToSerialsDetailsFragment(
                        it
                    )
                navController.navigate(action)
            }
        }
        if (intent != null && intent.hasExtra(PEOPLE_DATA_EXTRA)) {
            peopleName = intent.getStringExtra(PEOPLE_DATA_EXTRA)
            peopleName?.let {
                val action =
                    MovieDetailsFragmentDirections.actionMovieDetailsFragmentToPeopleDetailsFragment(
                        it
                    )
                navController.navigate(action)
            }
        }
    }
}
