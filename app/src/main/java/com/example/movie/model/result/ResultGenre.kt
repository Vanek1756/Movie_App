package com.example.movie.model.result

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultGenre(

    @Expose
    val id: Int,

    @Expose
    val name: String

) : Parcelable
