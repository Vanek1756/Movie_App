package com.example.movie.model.result

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultSerials(

    @Expose
    val backdrop_path: String?,

    @Expose
    val first_air_date: String,

    @Expose
    val genre_ids: List<Int>,

    @Expose
    val id: Int,

    @Expose
    val name: String,

    @Expose
    val origin_country: List<String>,

    @Expose
    val original_language: String,

    @Expose
    val original_name: String,

    @Expose
    val overview: String,

    @Expose
    val popularity: Double,

    @Expose
    val poster_path: String?,

    @Expose
    val vote_average: Double,

    @Expose
    val vote_count: Int

) : Parcelable
