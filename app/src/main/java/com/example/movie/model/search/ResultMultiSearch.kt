package com.example.movie.model.search

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultMultiSearch(

    @Expose
    val adult: Boolean?,

    @Expose
    val backdrop_path: String?,

    @Expose
    val first_air_date: String?,

    @Expose
    val genre_ids: List<Int>?,

    @Expose
    val id: Int?,

    @Expose
    val known_for: List<ResultMultiSearch>?,

    @Expose
    val media_type: String?,

    @Expose
    val name: String?,

    @Expose
    val origin_country: List<String>?,

    @Expose
    val original_language: String?,

    @Expose
    val original_name: String?,

    @Expose
    val original_title: String?,

    @Expose
    val overview: String?,

    @Expose
    val poster_path: String?,

    @Expose
    val profile_path: String?,

    @Expose
    val release_date: String?,

    @Expose
    val title: String?,

    @Expose
    val video: Boolean?,

    @Expose
    val vote_average: Number?,

    @Expose
    val vote_count: Int?

) : Parcelable
