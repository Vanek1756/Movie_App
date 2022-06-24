package com.example.movie.service

import com.example.movie.model.response.SerialsApiResponse
import com.example.movie.model.result.ResultSerials
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SerialsApiService {

    @GET("tv/popular")
    suspend fun getPopularSerials(): Response<SerialsApiResponse>

    @GET("tv/{tv_id}")
    suspend fun getDetailsSerials(@Path("tv_id") tv_id: Int): Response<ResultSerials>

    @POST("tv/airing_today")
    suspend fun getOnAirSerials(): Response<SerialsApiResponse>

    @POST("tv/top_rated")
    suspend fun getTheBestSerials(): Response<SerialsApiResponse>

    @POST("tv/on_the_air")
    suspend fun getTvSerials(): Response<SerialsApiResponse>
}
