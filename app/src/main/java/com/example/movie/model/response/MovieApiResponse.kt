package com.example.movie.model.response

import android.os.Parcelable
import com.example.movie.model.result.ResultFilm
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieApiResponse(

    @Expose
    val page: Int,

    @Expose
    val results: List<ResultFilm>,

    @Expose
    private var total_pages: Int,

    @Expose
    private var total_results: Int

) : Parcelable
