package com.example.movie.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieTokenResponse(

    @Expose
    private var success: Boolean,

    @Expose
    private var expires_at: String,

    @Expose
    var request_token: String

) : Parcelable
