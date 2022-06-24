package com.example.movie.service

import com.example.movie.model.response.PeopleApiResponse
import com.example.movie.model.result.ResultPeople
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PeopleApiService {

    @POST("person/popular")
    suspend fun getPopularPeople(): Response<PeopleApiResponse>

    @GET("person/{person_id}")
    suspend fun getDetailPeople(@Path("person_id") person_id: Int): Response<ResultPeople>
}
