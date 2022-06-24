package com.example.movie.model.result

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
class ResultFilm(

    @Expose
    val adult: Boolean?,

    @Expose
    val backdrop_path: String?,

    @Expose
    val genre_ids: List<Int>?,

    @Expose
    val id: Int?,

    @Expose
    val original_language: String?,

    @Expose
    val original_title: String?,

    @Expose
    val overview: String?,

    @Expose
    val popularity: Double?,

    @Expose
    val poster_path: String?,

    @Expose
    val release_date: String?,

    @Expose
    val title: String?,

    @Expose
    val video: Boolean?,

    @Expose
    val vote_average: Double?,

    @Expose
    val vote_count: Int?,

    @Expose
    val rating: Int?

) : Parcelable
