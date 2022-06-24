package com.example.movie.viewmodel

import androidx.lifecycle.ViewModel
import com.example.movie.model.User
import com.example.movie.model.repository.MovieRepository
import com.example.movie.model.repository.SearchRepository
import com.example.movie.model.repository.UserRepository
import com.example.movie.model.result.ResultFilm
import com.example.movie.model.result.ResultGenre
import com.example.movie.model.search.MultiSearchApiResponse
import com.example.movie.util.KEY_GENRE_ALL
import com.example.movie.util.KEY_SORT_POPULARITY
import com.example.movie.util.RetrofitListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FilmFragmentViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val userRepository: UserRepository,
    private val searchRepository: SearchRepository
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

    private var genre = KEY_GENRE_ALL
    private var sort = KEY_SORT_POPULARITY

    fun getSuccessRetrofitRequest(): StateFlow<Boolean?> = successRetrofitRequestFlow
    fun getErrorRetrofitRequest(): StateFlow<Throwable?> = errorRetrofitRequestFlow
    fun getWatchingNowFlow(): StateFlow<List<ResultFilm>?> = movieRepository.getWatchNowFlow()
    fun getWatchListFlow(): StateFlow<List<ResultFilm>?> = movieRepository.getWatchListFlow()
    fun getRatedFilmFlow(): StateFlow<List<ResultFilm>?> = movieRepository.getRatedMovieFlow()
    fun getGenreFilmFlow(): StateFlow<List<ResultGenre>?> = movieRepository.getGenreFlow()
    fun getSearchFlow(): StateFlow<MultiSearchApiResponse?> = searchRepository.getSearchFlow()
    fun getUserFlow(): StateFlow<User?> = userRepository.getUserFlow()
    fun getRecommendationsFilmFlow(): StateFlow<List<ResultFilm>?> =
        movieRepository.getRecommendationsFlow()

    fun getExpectedFlow(): StateFlow<List<ResultFilm>?> =
        movieRepository.getExpectedMovieFlow()

    fun getBestFilmFlow(): StateFlow<List<ResultFilm>?> =
        movieRepository.getTheBestMovieFlow()

    fun getFavoriteFilmFlow(): StateFlow<List<ResultFilm>?> =
        movieRepository.getFavoriteFlow()

    fun getPopularMovieFlow(): StateFlow<List<ResultFilm>?> =
        movieRepository.getPopularityMovieFlow()

    fun updateUser() = userRepository.getAccount()

    fun getGenreFilm() = movieRepository.getGenreMovies(listener)

    fun updatePopularFilm() {
        if (genre == KEY_GENRE_ALL && sort == KEY_SORT_POPULARITY) {
            movieRepository.getPopularMovies(listener)
        } else {
            if (genre == KEY_GENRE_ALL) {
                movieRepository.getFilmBySort(sort, listener)
            } else {
                movieRepository.getFilmByGenreAndSort(genre, sort, listener)
            }
        }
    }

    fun updateWatchingNow() = movieRepository.getWatchingNow(listener)

    fun updateExpected() = movieRepository.getExpected(listener)

    fun updateBestFilm() = movieRepository.getTheBestFilm(listener)

    fun updateFavoriteFilm(successListener: (status: Boolean) -> Unit) =
        movieRepository.getFavoriteMovies(listener, successListener)

    fun updateRatedFilm() = movieRepository.getRatedMovies(listener)

    fun updateWatchList() = movieRepository.getWatchList(listener)

    fun updateRecommendationsFilm() = movieRepository.getRecommendationsMovies(listener)

    fun singOutUser() = userRepository.deleteSession()

    fun multiSearch(textSearch: String) = searchRepository.multiSearch(textSearch)

    fun updateUi() = updatePopularFilm()

    fun setGenreFilm(genre: String) {
        this.genre = genre
    }

    fun setSortFilm(sort: String) {
        this.sort = sort
    }

    fun getGenre(): String = genre

    fun getSort() = sort

}