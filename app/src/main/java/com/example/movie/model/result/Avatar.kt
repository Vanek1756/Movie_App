package com.example.movie.model.result

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class Avatar(

    @Expose
    val gravatar: Gravatar,

    @Expose
    val tmdb: Tmdb

) : Parcelable

@Parcelize
data class Tmdb(

    @Expose
    val avatar_path: String

) : Parcelable
