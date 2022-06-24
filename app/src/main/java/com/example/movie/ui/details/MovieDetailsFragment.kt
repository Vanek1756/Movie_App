package com.example.movie.ui.details

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.databinding.MovieDetailsFragmentBinding
import com.example.movie.model.result.ResultFilm
import com.example.movie.model.result.ResultTrailerMovie
import com.example.movie.util.*
import com.example.movie.view.YouTubeActivity
import com.example.movie.viewmodel.DetailsFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import com.like.LikeButton
import com.like.OnLikeListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.movie_details_fragment) {

    @Inject
    lateinit var glideRequestManager: RequestManager

    private val binding by viewBinding(MovieDetailsFragmentBinding::bind)

    private var resultTrailerList: ArrayList<ResultTrailerMovie>? = null
    private var filmId: Int? = null
    private var resultFilm: ResultFilm? = null

    private val fragmentViewModel by viewModels<DetailsFragmentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pbDetailsFilm.show()
        filmId = arguments?.let { MovieDetailsFragmentArgs.fromBundle(it).filmId }
        filmId?.let { fragmentViewModel.getDetailsFilm(it) }
        fragmentViewModel.getSuccessRetrofitRequest().onEach { success ->
            success?.let {
                fragmentViewModel.getDetailFilmFlow().onEach {
                    resultFilm = it
                    updateViewModel()
                    listenerForBtn()
                }.launchWhenStarted(lifecycleScope)
            }
        }.launchWhenStarted(lifecycleScope)
    }

    @SuppressLint("SetTextI18n")
    private fun fillView() {
        with(binding) {
            val imagePath = getString(R.string.adapter_image_path_w500) +
                    resultFilm?.poster_path
            ivPosterFilm.glidePoster(imagePath, glideRequestManager)
            val imageTintPath = getString(R.string.adapter_image_path) +
                    resultFilm?.backdrop_path
            ivTintFilm.glideTint(imageTintPath, glideRequestManager)

            progressBar.max = 100
            val progress = resultFilm?.vote_average.toString().getProgress()
            progressBar.progress = progress.toInt()
            tvProgress.text = progress + getString(R.string.percent)

            tvOverviewFilm.text = resultFilm?.overview
            tvTitleFilm.text = resultFilm?.title
            tvReleaseDateFilm.text =
                "(" + resultFilm?.release_date?.substringBefore('-') + ")"
        }
    }

    private fun listenerForBtn() {
        with(binding) {
            btnAddToFavorite.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton) {
                    fragmentViewModel.addFavoriteFilm(resultFilm?.id!!, true)
                }

                override fun unLiked(likeButton: LikeButton) {
                    fragmentViewModel.addFavoriteFilm(resultFilm?.id!!, false)
                }
            })
            btnAddToWatchList.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton) {
                    fragmentViewModel.addWatchListFilm(resultFilm?.id!!, true)
                }

                override fun unLiked(likeButton: LikeButton) {
                    fragmentViewModel.addWatchListFilm(resultFilm?.id!!, false)
                }
            })
            btnPlayYoutube.setOnClickListener { openYoutube() }
            ratingBar.onRatingBarChangeListener =
                OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    if (rating < 0.5) {
                        fragmentViewModel.deleteRatingFilm(resultFilm?.id!!)
                    } else {
                        fragmentViewModel.addRatingFilm(resultFilm?.id!!, rating * 2)
                    }
                }
        }
    }

    private fun openYoutube() {
        if (resultTrailerList?.isNotEmpty() == true) {
            val intent = Intent(context, YouTubeActivity::class.java)
            intent.putExtra(YouTubeActivity.YOUTUBE_EXTRA, resultTrailerList?.get(0))
            startActivity(intent)
        } else {
            Snackbar.make(
                binding.root,
                getString(R.string.no_trailer),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun updateViewModel() {
        fragmentViewModel.updateFavoriteFilm() {}
        fragmentViewModel.updateRatedFilm()
        fragmentViewModel.updateWatchList()
        filmId?.let { fragmentViewModel.updateTrailerMovie(it) }
        fragmentViewModel.getRatedFilmFlow().onEach { results ->
            results?.forEach {
                if (it.id == filmId) {
                    var rating: Float = (it.rating!!).toFloat()
                    rating /= 2
                    binding.ratingBar.rating = rating
                }
            }
        }.launchWhenStarted(lifecycleScope)
        fragmentViewModel.getTrailerMovieFlow().onEach { results ->
            results?.let { resultTrailerList = it as ArrayList<ResultTrailerMovie> }
        }.launchWhenStarted(lifecycleScope)
        fragmentViewModel.getWatchListFlow().onEach { results ->
            var active = false
            results?.forEach {
                if (it.id == resultFilm?.id) {
                    active = true
                }
            }
            binding.btnAddToWatchList.isLiked = active
        }.launchWhenStarted(lifecycleScope)
        fragmentViewModel.getFavoriteFilmFlow().onEach { results ->
            var active = false
            results?.forEach {
                if (it.id == resultFilm?.id) {
                    active = true
                }
            }
            binding.btnAddToFavorite.isLiked = active
        }.launchWhenStarted(lifecycleScope)
        binding.pbDetailsFilm.hide()
        fillView()
    }
}
