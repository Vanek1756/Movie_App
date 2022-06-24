package com.example.movie.model.repository

import com.example.movie.model.User
import com.example.movie.model.result.ResultFilm
import com.example.movie.model.result.ResultGenre
import com.example.movie.model.result.ResultTrailerMovie
import com.example.movie.service.MovieApiService
import com.example.movie.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Singleton
class MovieRepository @Inject constructor(
    private val movieApiService: MovieApiService,
    private val user: User
) {
    private val uiScope = CoroutineScope(Dispatchers.Main)

    private var trailerMovie: List<ResultTrailerMovie>? = null
    private var favoriteMovie: List<ResultFilm>? = null
    private var recommendationsMovie: List<ResultFilm>? = null
    private var watchListMovie: List<ResultFilm>? = null
    private var watchNowMovie: List<ResultFilm>? = null
    private var ratedMovie: List<ResultFilm>? = null
    private var genreMovie: List<ResultGenre>? = null
    private var detailsMovie: ResultFilm? = null
    private var popularityMovie: List<ResultFilm>? = null
    private var expectedMovie: List<ResultFilm>? = null
    private var theBestMovie: List<ResultFilm>? = null

    private val mutableFavoriteFlow: MutableStateFlow<List<ResultFilm>?> = MutableStateFlow(null)
    private val mutableWatchListFLow: MutableStateFlow<List<ResultFilm>?> = MutableStateFlow(null)
    private val mutableWatchNowFlow: MutableStateFlow<List<ResultFilm>?> = MutableStateFlow(null)
    private val mutableRatedMovieFlow: MutableStateFlow<List<ResultFilm>?> = MutableStateFlow(null)
    private val mutableGenreFlow: MutableStateFlow<List<ResultGenre>?> = MutableStateFlow(null)
    private val mutableDetailsFlow: MutableStateFlow<ResultFilm?> = MutableStateFlow(null)
    private val mutableExpectedFlow: MutableStateFlow<List<ResultFilm>?> = MutableStateFlow(null)
    private val mutableTheBestFlow: MutableStateFlow<List<ResultFilm>?> = MutableStateFlow(null)
    private val mutableTrailerMovieFlow: MutableStateFlow<List<ResultTrailerMovie>?> =
        MutableStateFlow(null)
    private val mutableRecommendationsFlow: MutableStateFlow<List<ResultFilm>?> =
        MutableStateFlow(null)
    private val mutablePopularityMovieFlow: MutableStateFlow<List<ResultFilm>?> =
        MutableStateFlow(null)

    fun getGenreFlow(): StateFlow<List<ResultGenre>?> = mutableGenreFlow
    fun getDetailsFlow(): StateFlow<ResultFilm?> = mutableDetailsFlow
    fun getPopularityMovieFlow(): StateFlow<List<ResultFilm>?> = mutablePopularityMovieFlow
    fun getFavoriteFlow(): StateFlow<List<ResultFilm>?> = mutableFavoriteFlow
    fun getRecommendationsFlow(): StateFlow<List<ResultFilm>?> = mutableRecommendationsFlow
    fun getWatchListFlow(): StateFlow<List<ResultFilm>?> = mutableWatchListFLow
    fun getWatchNowFlow(): StateFlow<List<ResultFilm>?> = mutableWatchNowFlow
    fun getRatedMovieFlow(): StateFlow<List<ResultFilm>?> = mutableRatedMovieFlow
    fun getTrailerMovieFlow(): StateFlow<List<ResultTrailerMovie>?> = mutableTrailerMovieFlow
    fun getExpectedMovieFlow(): StateFlow<List<ResultFilm>?> = mutableExpectedFlow
    fun getTheBestMovieFlow(): StateFlow<List<ResultFilm>?> = mutableTheBestFlow

    private var movieId = 0

    fun getTrailerMovie(
        id: Int,
        listener: RetrofitListener?
    ) {
        uiScope.launch {
            safeApiCall(
                { movieApiService.getTrailerVideo(id) },
                success = { response ->
                    response.body()?.let { trailerMovie = it.results }
                    trailerMovie?.let {
                        mutableTrailerMovieFlow.value = it
                        listener?.onSuccess()
                    }
                },
                error = {
                    trailerMovie = null
                    listener?.onError(Throwable(it))
                }
            )
        }
    }

    fun getGenreMovies(listener: RetrofitListener) {
        uiScope.launch {
            safeApiCall(
                { movieApiService.getGenreMovies() },
                success = { response ->
                    response.body()?.let { genreMovie = it.genres }
                    genreMovie?.let {
                        mutableGenreFlow.value = it
                        listener.onSuccess()
                    }
                },
                error = {
                    genreMovie = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }

    fun getPopularMovies(listener: RetrofitListener) {
        uiScope.launch {
            safeApiCall(
                { movieApiService.getPopularMovies() },
                success = { response ->
                    response.body()?.let { popularityMovie = it.results }
                    popularityMovie?.let {
                        mutablePopularityMovieFlow.value = it
                        listener.onSuccess()
                    }
                },
                error = {
                    popularityMovie = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }

    fun getWatchList(listener: RetrofitListener?) {
        uiScope.launch {
            safeApiCall(
                { movieApiService.getWatchList(user.id ?: 0, user.sessionId ?: "") },
                success = { response ->
                    response.body()?.let { watchListMovie = it.results }
                    watchListMovie?.let {
                        mutableWatchListFLow.value = it
                        listener?.onSuccess()
                    }
                },
                error = {
                    watchListMovie = null
                    listener?.onError(Throwable(it))
                }
            )
        }
    }

    fun getFavoriteMovies(
        listener: RetrofitListener?,
        successListener: (status: Boolean) -> Unit
    ) {
        uiScope.launch {
            safeApiCall(
                { movieApiService.getFavoriteMovies(user.id.toString(), user.sessionId ?: "") },
                success = { response ->
                    response.body()?.let { favoriteMovie = it.results }
                    favoriteMovie?.let { resultList ->
                        mutableFavoriteFlow.value = resultList
                        favoriteMovie?.get(0)?.id?.let { movieId = it }
                        successListener(true)
                        listener?.onSuccess()
                    }
                },
                error = {
                    favoriteMovie = null
                    listener?.onError(Throwable(it))
                }
            )
        }
    }

    fun getRatedMovies(listener: RetrofitListener?) {
        uiScope.launch {
            safeApiCall(
                { movieApiService.getRatedMovies(user.id ?: 0, user.sessionId ?: "") },
                success = { response ->
                    response.body()?.let { ratedMovie = it.results }
                    ratedMovie?.let {
                        mutableRatedMovieFlow.value = it
                        listener?.onSuccess()
                    }
                },
                error = {
                    ratedMovie = null
                    listener?.onError(Throwable(it))
                }
            )
        }
    }

    fun getRecommendationsMovies(listener: RetrofitListener) {
        getFavoriteMovies(listener) {
            uiScope.launch {
                safeApiCall(
                    { movieApiService.getRecommendationsMovies(movieId.toString()) },
                    success = { response ->
                        response.body()?.let { recommendationsMovie = it.results }
                        recommendationsMovie?.let {
                            mutableRecommendationsFlow.value = it
                            listener.onSuccess()
                        }
                    },
                    error = {
                        recommendationsMovie = null
                        listener.onError(Throwable(it))
                    }
                )
            }
        }
    }

    fun getFilmByGenreAndSort(
        genre: String,
        sort: String,
        listener: RetrofitListener
    ) {
        var querySort = ""
        when (sort) {
            KEY_SORT_POPULARITY -> querySort = "popularity.desc"
            KEY_SORT_RATING -> querySort = "vote_count.desc"
            KEY_SORT_RELEASE_DATE -> querySort = "primary_release_date.desc"
        }
        uiScope.launch {
            safeApiCall(
                { movieApiService.getFilmByGenreAndSort(querySort, genre) },
                success = { response ->
                    response.body()?.let { popularityMovie = it.results }
                    popularityMovie?.let {
                        mutablePopularityMovieFlow.value = it
                        listener.onSuccess()
                    }
                },
                error = {
                    popularityMovie = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }

    fun getWatchingNow(listener: RetrofitListener) {
        uiScope.launch {
            safeApiCall(
                { movieApiService.getWatchingNow() },
                success = { response ->
                    response.body()?.let { watchNowMovie = it.results }
                    watchNowMovie?.let {
                        mutableWatchNowFlow.value = it
                        listener.onSuccess()
                    }
                },
                error = {
                    watchNowMovie = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }

    fun getExpected(listener: RetrofitListener) {
        uiScope.launch {
            safeApiCall(
                { movieApiService.getExpected() },
                success = { response ->
                    response.body()?.let { expectedMovie = it.results }
                    expectedMovie?.let {
                        mutableExpectedFlow.value = it
                        listener.onSuccess()
                    }
                },
                error = {
                    expectedMovie = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }

    fun getTheBestFilm(listener: RetrofitListener) {
        uiScope.launch {
            safeApiCall(
                { movieApiService.getTheBestFilm() },
                success = { response ->
                    response.body()?.let { theBestMovie = it.results }
                    theBestMovie?.let {
                        mutableTheBestFlow.value = it
                        listener.onSuccess()
                    }
                },
                error = {
                    theBestMovie = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }

    fun getDetailsMovie(id: Int, listener: RetrofitListener) {
        uiScope.launch {
            safeApiCall(
                { movieApiService.getDetailsMovies(id) },
                success = { response ->
                    response.body()?.let { detailsMovie = it }
                    detailsMovie?.let {
                        mutableDetailsFlow.value = it
                        listener.onSuccess()
                    }
                },
                error = {
                    detailsMovie = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }

    fun getFilmBySort(
        sort: String,
        listener: RetrofitListener
    ) {
        var querySort = ""
        when (sort) {
            KEY_SORT_POPULARITY -> querySort = "popularity.desc"
            KEY_SORT_RATING -> querySort = "vote_average.desc"
            KEY_SORT_RELEASE_DATE -> querySort = "release_date.desc"
        }
        uiScope.launch {
            safeApiCall(
                { movieApiService.getFilmBySort(querySort) },
                success = { response ->
                    response.body()?.let { popularityMovie = it.results }
                    popularityMovie?.let {
                        mutablePopularityMovieFlow.value = it
                        listener.onSuccess()
                    }
                },
                error = {
                    popularityMovie = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }
}
