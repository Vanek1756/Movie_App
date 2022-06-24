package com.example.movie.model.result

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gravatar(

    @Expose
    val hash: String

) : Parcelable
