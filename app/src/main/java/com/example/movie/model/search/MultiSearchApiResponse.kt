package com.example.movie.model.search

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class MultiSearchApiResponse(

    @Expose
    val page: Int,

    @Expose
    val results: List<ResultMultiSearch>?,

    @Expose
    val total_pages: Int,

    @Expose
    val total_results: Int

) : Parcelable
