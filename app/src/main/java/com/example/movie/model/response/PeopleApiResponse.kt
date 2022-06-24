package com.example.movie.model.response

import android.os.Parcelable
import com.example.movie.model.result.ResultPeople
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class PeopleApiResponse(

    @Expose
    val page: Int,

    @Expose
    val results: List<ResultPeople>,

    @Expose
    val total_pages: Int,

    @Expose
    val total_results: Int

) : Parcelable
