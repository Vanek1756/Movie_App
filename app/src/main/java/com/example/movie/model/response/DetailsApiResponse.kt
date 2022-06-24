package com.example.movie.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailsApiResponse(

    @Expose
    val adult: Boolean,

    @Expose
    val backdrop_path: String,

    @Expose
    val id: Int,

    @Expose
    val original_language: String,

    @Expose
    val original_title: String,

    @Expose
    val overview: String,

    @Expose
    val poster_path: String,

    @Expose
    val release_date: String,

    @Expose
    val title: String,

    @Expose
    val vote_average: Double

) : Parcelable
