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
import com.example.movie.databinding.WatchingNowFragmentBinding
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
class WatchingNowFilmFragment : Fragment(R.layout.watching_now_fragment) {

    @Inject
    lateinit var glideRequestManager: RequestManager

    private val binding by viewBinding(WatchingNowFragmentBinding::bind)

    private lateinit var resultFilmArrayList: ArrayList<ResultFilm>
    private val fragmentViewModel by activityViewModels<FilmFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            pbWatchingNow.show()
            fragmentViewModel.updateWatchingNow()
            fragmentViewModel.getSuccessRetrofitRequest().onEach { success ->
                success?.let {
                    fragmentViewModel.getWatchingNowFlow().onEach { results ->
                        results?.let { resultFilmArrayList = it as ArrayList<ResultFilm>
                            watchingNowSwipeRefresh.isRefreshing = false
                            pbWatchingNow.hide()
                            tvNotConnect.visibility = View.GONE
                            btnRetryConnect.visibility = View.GONE
                            rvWatchingNow.visibility = View.VISIBLE
                            fillRecyclerView() }
                        }.launchWhenStarted(lifecycleScope)
                }
            }.launchWhenStarted(lifecycleScope)
            fragmentViewModel.getErrorRetrofitRequest().onEach {
                it?.let {
                    it.toastError(requireContext())
                    watchingNowSwipeRefresh.isRefreshing = false
                    pbWatchingNow.hide()
                    tvNotConnect.visibility = View.VISIBLE
                    btnRetryConnect.visibility = View.VISIBLE
                    rvWatchingNow.visibility = View.GONE
                }
            }.launchWhenStarted(lifecycleScope)

            btnRetryConnect.setOnClickListener {
                fragmentViewModel.updateWatchingNow()
                tvNotConnect.visibility = View.GONE
                btnRetryConnect.visibility = View.GONE
                pbWatchingNow.show()
            }

            watchingNowSwipeRefresh.setColorSchemeResources(R.color.colorPrimary)
            watchingNowSwipeRefresh.setOnRefreshListener { fragmentViewModel.updateWatchingNow() }
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
                rvWatchingNow.layoutManager = GridLayoutManager(context, 2)
            } else {
                rvWatchingNow.layoutManager = GridLayoutManager(context, 4)
            }

            rvWatchingNow.itemAnimator = DefaultItemAnimator()
            rvWatchingNow.adapter = filmAdapter
            filmAdapter.notifyDataSetChanged()
            watchingNowSwipeRefresh.isRefreshing = false
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
