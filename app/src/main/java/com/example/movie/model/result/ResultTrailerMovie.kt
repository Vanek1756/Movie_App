package com.example.movie.model.result

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultTrailerMovie(

    @Expose
    val id: String,

    @Expose
    val iso_3166_1: String,

    @Expose
    val iso_639_1: String,

    @Expose
    val key: String,

    @Expose
    val name: String,

    @Expose
    val official: Boolean,

    @Expose
    val published_at: String,

    @Expose
    val site: String,

    @Expose
    val size: Int,

    @Expose
    val type: String

) : Parcelable
