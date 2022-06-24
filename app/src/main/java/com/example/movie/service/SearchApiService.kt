package com.example.movie.service

import com.example.movie.model.search.MultiSearchApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {

    @GET("search/multi")
    suspend fun multiSearch(@Query("query") query: String): Response<MultiSearchApiResponse>
}
