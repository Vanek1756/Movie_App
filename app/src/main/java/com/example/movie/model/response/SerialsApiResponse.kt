package com.example.movie.model.response

import android.os.Parcelable
import com.example.movie.model.result.ResultSerials
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class SerialsApiResponse(

    @Expose
    val page: Int,

    @Expose
    val results: List<ResultSerials>,

    @Expose
    val total_pages: Int,

    @Expose
    val total_results: Int

) : Parcelable
