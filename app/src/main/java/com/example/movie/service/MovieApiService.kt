package com.example.movie.service

import com.example.movie.model.response.GenreApiResponse
import com.example.movie.model.response.MovieApiResponse
import com.example.movie.model.response.TrailerMovieApiResponse
import com.example.movie.model.result.ResultFilm
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("genre/movie/list")
    suspend fun getGenreMovies(): Response<GenreApiResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(): Response<MovieApiResponse>

    @GET("movie/{movie_id}")
    suspend fun getDetailsMovies(@Path("movie_id") movie_id: Int): Response<ResultFilm>

    @GET("account/{account_id}/watchlist/movies")
    suspend fun getWatchList(
        @Path("account_id") account_id: Int,
        @Query("session_id") session_id: String
    ): Response<MovieApiResponse>

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavoriteMovies(
        @Path("account_id") account_id: String,
        @Query("session_id") session_id: String
    ): Response<MovieApiResponse>

    @GET("account/{account_id}/rated/movies")
    suspend fun getRatedMovies(
        @Path("account_id") account_id: Int,
        @Query("session_id") session_id: String
    ): Response<MovieApiResponse>

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendationsMovies(@Path("movie_id") movie_id: String): Response<MovieApiResponse>

    @GET("discover/movie")
    suspend fun getFilmByGenreAndSort(
        @Query("sort_by") sort_by: String,
        @Query("with_genres") with_genres: String
    ): Response<MovieApiResponse>

    @GET("discover/movie")
    suspend fun getFilmBySort(@Query("sort_by") sort_by: String): Response<MovieApiResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getTrailerVideo(@Path("movie_id") movie_id: Int): Response<TrailerMovieApiResponse>

    @POST("movie/now_playing")
    suspend fun getWatchingNow(): Response<MovieApiResponse>

    @POST("movie/top_rated")
    suspend fun getTheBestFilm(): Response<MovieApiResponse>

    @POST("movie/upcoming")
    suspend fun getExpected(): Response<MovieApiResponse>
}
