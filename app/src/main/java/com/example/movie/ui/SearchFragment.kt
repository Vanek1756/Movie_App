package com.example.movie.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.adapter.SearchAdapter
import com.example.movie.databinding.SearchFragmentBinding
import com.example.movie.model.search.ResultMultiSearch
import com.example.movie.util.launchWhenStarted
import com.example.movie.util.viewBinding
import com.example.movie.view.DetailsActivity
import com.example.movie.viewmodel.FilmFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment) {

    @Inject
    lateinit var glideRequestManager: RequestManager

    private val binding by viewBinding(SearchFragmentBinding::bind)

    private val fragmentViewModel by viewModels<FilmFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnSearch.setOnClickListener { startSearch() }
            fragmentViewModel.getSearchFlow().onEach { result ->
                result?.let { fillView(it.results as ArrayList<ResultMultiSearch>, it.total_results) }
                }.launchWhenStarted(lifecycleScope)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fillView(arrayList: ArrayList<ResultMultiSearch>, totalResults: Int) {
        if (totalResults > 0) {
            val filmAdapter = SearchAdapter(
                arrayList,
                glideRequestManager
            ) { film, view, text ->
                startDetails(film, view, text)
            }

            with(binding) {
                rvSearch.visibility = View.VISIBLE
                ivNoSearch.visibility = View.GONE
                tvNoSearch.visibility = View.GONE

                if (resources.configuration.orientation ==
                    Configuration.ORIENTATION_PORTRAIT
                ) {
                    rvSearch.layoutManager = GridLayoutManager(context, 2)
                } else {
                    rvSearch.layoutManager = GridLayoutManager(context, 4)
                }

                rvSearch.itemAnimator = DefaultItemAnimator()
                rvSearch.adapter = filmAdapter
                filmAdapter.notifyDataSetChanged()
            }
        } else {
            with(binding) {
                rvSearch.visibility = View.GONE
                ivNoSearch.visibility = View.VISIBLE
                tvNoSearch.visibility = View.VISIBLE
                ivNoSearch.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.no_search
                    )
                )
            }
        }
    }

    private fun startSearch() {
        val textSearch = binding.etSearch.text.toString()
        if (textSearch.length > 2) {
            fragmentViewModel.multiSearch(textSearch)
        }
    }

    private fun startDetails(media: ResultMultiSearch, view: View, text: TextView) {
        val ivPair =
            androidx.core.util.Pair.create<View, String>(view, "ivMovieToDetail")
        val tvPair =
            androidx.core.util.Pair.create<View, String>(text, "tvTitleMovieToDetail")

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this.requireActivity(),
            ivPair,
            tvPair
        )

        when (media.media_type) {
            "movie" -> {
                media.id?.let {
                    val intent = Intent(requireContext(), DetailsActivity::class.java)
                    intent.putExtra(DetailsActivity.MOVIE_DATA_EXTRA, it)
                    startActivity(intent, options.toBundle())
                }
            }
            "tv" -> {
                media.id?.let {
                    val intent = Intent(requireContext(), DetailsActivity::class.java)
                    intent.putExtra(DetailsActivity.SERIALS_DATA_EXTRA, it)
                    startActivity(intent, options.toBundle())
                }
            }
            "person" -> {
                media.name?.let {
                    val intent = Intent(requireContext(), DetailsActivity::class.java)
                    intent.putExtra(DetailsActivity.PEOPLE_DATA_EXTRA, it)
                    startActivity(intent, options.toBundle())
                }
            }
        }
    }
}
