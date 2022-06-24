package com.example.movie.viewmodel

import androidx.lifecycle.ViewModel
import com.example.movie.model.repository.MovieRepository
import com.example.movie.model.repository.SerialsRepository
import com.example.movie.model.repository.UserRepository
import com.example.movie.model.result.ResultFilm
import com.example.movie.model.result.ResultSerials
import com.example.movie.model.result.ResultTrailerMovie
import com.example.movie.util.RetrofitListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val userRepository: UserRepository,
    private val serialsRepository: SerialsRepository
) : ViewModel() {

    private val successRetrofitRequestFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    private val errorRetrofitRequestFlow: MutableStateFlow<Throwable?> = MutableStateFlow(null)

    private val listener = object : RetrofitListener {
        override fun onSuccess() {
            successRetrofitRequestFlow.value = true
            errorRetrofitRequestFlow.value = null
        }

        override fun onError(throwable: Throwable) {
            errorRetrofitRequestFlow.value = throwable
            successRetrofitRequestFlow.value = null
        }
    }

    fun getDetailFilmFlow(): StateFlow<ResultFilm?> = movieRepository.getDetailsFlow()
    fun getWatchListFlow(): StateFlow<List<ResultFilm>?> = movieRepository.getWatchListFlow()
    fun getRatedFilmFlow(): StateFlow<List<ResultFilm>?> = movieRepository.getRatedMovieFlow()
    fun getDetailSerialsFlow(): StateFlow<ResultSerials?> = serialsRepository.getDetailsFlow()
    fun getErrorRetrofitRequest(): StateFlow<Throwable?> = errorRetrofitRequestFlow
    fun getSuccessRetrofitRequest(): StateFlow<Boolean?> = successRetrofitRequestFlow
    fun getTrailerMovieFlow(): StateFlow<List<ResultTrailerMovie>?> =
        movieRepository.getTrailerMovieFlow()

    fun getFavoriteFilmFlow(): StateFlow<List<ResultFilm>?> =
        movieRepository.getFavoriteFlow()


    fun updateWatchList() = movieRepository.getWatchList(null)

    fun deleteRatingFilm(id: Int) = userRepository.deleteRatingFilm(id)

    fun updateRatedFilm() = movieRepository.getRatedMovies(null)

    fun getDetailsFilm(id: Int) = movieRepository.getDetailsMovie(id, listener)

    fun getDetailsSerials(id: Int) = serialsRepository.getDetailsSerials(id, listener)

    fun updateTrailerMovie(id: Int) = movieRepository.getTrailerMovie(id, null)

    fun addRatingFilm(id: Int, rating: Number) = userRepository.addRatingFilm(id, rating)

    fun addFavoriteFilm(id: Int, favorite: Boolean) =
        userRepository.markAsFavoriteMovie(id, favorite)

    fun addWatchListFilm(id: Int, watchlist: Boolean) =
        userRepository.addWatchListMovie(id, watchlist)

    fun updateFavoriteFilm(successListener: (status: Boolean) -> Unit) =
        movieRepository.getFavoriteMovies(null, successListener)

}