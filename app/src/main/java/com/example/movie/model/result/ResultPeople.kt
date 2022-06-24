package com.example.movie.model.result

import android.os.Parcelable
import com.example.movie.model.search.ResultMultiSearch
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultPeople(

    @Expose
    val adult: Boolean?,

    @Expose
    val also_known_as: List<String>?,

    @Expose
    val id: Int?,

    @Expose
    val known_for: List<ResultMultiSearch>?,

    @Expose
    val name: String?,

    @Expose
    val birthday: String?,

    @Expose
    val biography: String?,

    @Expose
    val popularity: Double?,

    @Expose
    val profile_path: String?

) : Parcelable
