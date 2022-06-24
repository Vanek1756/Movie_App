package com.example.movie.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.adapter.FilmAdapter
import com.example.movie.databinding.ProfileRatingsFragmentBinding
import com.example.movie.model.result.ResultFilm
import com.example.movie.util.launchWhenStarted
import com.example.movie.util.viewBinding
import com.example.movie.view.DetailsActivity
import com.example.movie.viewmodel.FilmFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RatingsFragment : Fragment(R.layout.profile_ratings_fragment) {

    @Inject
    lateinit var glideRequestManager: RequestManager

    private val binding by viewBinding(ProfileRatingsFragmentBinding::bind)

    private lateinit var resultFilmArrayList: ArrayList<ResultFilm>
    private val fragmentViewModel by viewModels<FilmFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentViewModel.updateRatedFilm()
        fragmentViewModel.getSuccessRetrofitRequest().onEach { success ->
            success?.let {
                fragmentViewModel.getRatedFilmFlow().onEach { results ->
                    results?.let {
                        resultFilmArrayList = it as ArrayList<ResultFilm>
                        fillRecyclerView()
                    }
                }.launchWhenStarted(lifecycleScope)
            }
        }.launchWhenStarted(lifecycleScope)
        with(binding) {
            ratingsFilmSwipeRefresh.setColorSchemeResources(R.color.colorPrimary)
            ratingsFilmSwipeRefresh.setOnRefreshListener { fragmentViewModel.updateRecommendationsFilm() }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fillRecyclerView() {

        val filmAdapter = FilmAdapter(
            resultFilmArrayList,
            glideRequestManager
        ) { film, view, text ->
            startDetails(film, view, text)
        }

        with(binding) {
            if (resources.configuration.orientation ==
                Configuration.ORIENTATION_PORTRAIT
            ) {
                rvRatingsFilm.layoutManager = GridLayoutManager(context, 2)
            } else {
                rvRatingsFilm.layoutManager = GridLayoutManager(context, 4)
            }

            rvRatingsFilm.itemAnimator = DefaultItemAnimator()
            rvRatingsFilm.adapter = filmAdapter
            filmAdapter.notifyDataSetChanged()
            ratingsFilmSwipeRefresh.isRefreshing = false
        }
    }

    private fun startDetails(film: ResultFilm, view: View, text: TextView) {
        val intent = Intent(context, DetailsActivity::class.java)

        val ivPair = androidx.core.util.Pair.create<View, String>(view, "ivMovieToDetail")
        val tvPair = androidx.core.util.Pair.create<View, String>(text, "tvTitleMovieToDetail")

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this.requireActivity(),
            ivPair,
            tvPair
        )

        intent.putExtra(DetailsActivity.MOVIE_DATA_EXTRA, film.id)
        startActivity(intent, options.toBundle())
    }
}
