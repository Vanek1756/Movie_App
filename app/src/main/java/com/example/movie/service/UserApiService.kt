package com.example.movie.service

import com.example.movie.model.response.MovieTokenResponse
import com.example.movie.model.response.SessionResponse
import com.example.movie.model.result.ResultUser
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {

    @GET("authentication/token/new")
    suspend fun getToken(): Response<MovieTokenResponse>

    @POST("authentication/token/validate_with_login")
    suspend fun authenticationWithLogin(@Body requestBody: RequestBody): Response<SessionResponse>

    @POST("authentication/session/new")
    suspend fun newSession(@Body requestBody: RequestBody): Response<SessionResponse>

    @GET("account")
    suspend fun getAccount(@Query("session_id") session_id: String): Response<ResultUser>

    @POST("account/{account_id}/favorite")
    suspend fun markAsFavorite(
        @Path("account_id") account_id: Int,
        @Query("session_id") session_id: String,
        @Body requestBody: RequestBody
    ): Response<Unit>

    @POST("account/{account_id}/watchlist")
    suspend fun addWatchList(
        @Path("account_id") account_id: Int,
        @Query("session_id") session_id: String,
        @Body requestBody: RequestBody
    ): Response<Unit>

    @POST("movie/{movie_id}/rating")
    suspend fun addRatingFilm(
        @Path("movie_id") movie_id: Int,
        @Query("session_id") session_id: String,
        @Body requestBody: RequestBody
    ): Response<Unit>

    @DELETE("movie/{movie_id}/rating")
    suspend fun deleteRatingFilm(
        @Path("movie_id") movie_id: Int,
        @Query("session_id") session_id: String
    ): Response<Unit>
}
