package com.example.movie.model.response

import android.os.Parcelable
import com.example.movie.model.result.ResultTrailerMovie
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrailerMovieApiResponse(

    @Expose
    val id: Int,

    @Expose
    val results: List<ResultTrailerMovie>

) : Parcelable
