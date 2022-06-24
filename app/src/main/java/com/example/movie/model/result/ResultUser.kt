package com.example.movie.model.result

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultUser(

    @Expose
    val avatar: Avatar,

    @Expose
    val id: Int,

    @Expose
    val include_adult: Boolean,

    @Expose
    val iso_3166_1: String,

    @Expose
    val iso_639_1: String,

    @Expose
    val name: String,

    @Expose
    val username: String

) : Parcelable
