package com.example.movie.ui.films

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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.adapter.FilmAdapter
import com.example.movie.databinding.PopularFilmFragmentBinding
import com.example.movie.model.result.ResultFilm
import com.example.movie.util.launchWhenStarted
import com.example.movie.util.toastError
import com.example.movie.util.viewBinding
import com.example.movie.view.DetailsActivity
import com.example.movie.viewmodel.FilmFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PopularFilmFragment : Fragment(R.layout.popular_film_fragment) {

    @Inject
    lateinit var glideRequestManager: RequestManager

    private val binding by viewBinding(PopularFilmFragmentBinding::bind)

    private lateinit var resultFilmArrayList: ArrayList<ResultFilm>
    private val fragmentViewModel by activityViewModels<FilmFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            pbPopularFilm.show()
            fragmentViewModel.updatePopularFilm()
            fragmentViewModel.getSuccessRetrofitRequest().onEach {
                it?.let {
                    fragmentViewModel.getPopularMovieFlow().onEach { results ->
                        resultFilmArrayList = results as ArrayList<ResultFilm>
                        popularFilmSwipeRefresh.isRefreshing = false
                        pbPopularFilm.hide()
                        tvNotConnect.visibility = View.GONE
                        btnRetryConnect.visibility = View.GONE
                        rvPopularFilm.visibility = View.VISIBLE
                        fillRecyclerView()
                    }.launchWhenStarted(lifecycleScope)
                }
            }.launchWhenStarted(lifecycleScope)
            fragmentViewModel.getErrorRetrofitRequest().onEach {
                it?.let {
                    it.toastError(requireContext())
                    popularFilmSwipeRefresh.isRefreshing = false
                    pbPopularFilm.hide()
                    tvNotConnect.visibility = View.VISIBLE
                    btnRetryConnect.visibility = View.VISIBLE
                    rvPopularFilm.visibility = View.GONE
                }
            }.launchWhenStarted(lifecycleScope)

            btnRetryConnect.setOnClickListener {
                fragmentViewModel.updatePopularFilm()
                tvNotConnect.visibility = View.GONE
                btnRetryConnect.visibility = View.GONE
                pbPopularFilm.show()
            }

            popularFilmSwipeRefresh.setColorSchemeResources(R.color.colorPrimary)
            popularFilmSwipeRefresh.setOnRefreshListener { fragmentViewModel.updatePopularFilm() }
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
                rvPopularFilm.layoutManager = GridLayoutManager(context, 2)
            } else {
                rvPopularFilm.layoutManager = GridLayoutManager(context, 4)
            }

            rvPopularFilm.itemAnimator = DefaultItemAnimator()
            rvPopularFilm.adapter = filmAdapter
            filmAdapter.notifyDataSetChanged()
            popularFilmSwipeRefresh.isRefreshing = false
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
