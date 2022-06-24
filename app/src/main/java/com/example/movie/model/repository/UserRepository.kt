package com.example.movie.model.repository

import com.example.movie.data.Storage
import com.example.movie.model.User
import com.example.movie.service.UserApiService
import com.example.movie.util.RetrofitListener
import com.example.movie.util.safeApiCall
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class UserRepository @Inject constructor(
    private val userApiService: UserApiService,
    private val storage: Storage,
    private val user: User
) {
    private val uiScope = CoroutineScope(Dispatchers.Main)

    private var requestToken: String? = null
    private var sessionId: String? = null

    private val sessionSuccessFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    private val userFlow: MutableStateFlow<User?> = MutableStateFlow(null)

    fun getSessionSuccessFlow(): StateFlow<Boolean?> = sessionSuccessFlow
    fun getUserFlow(): StateFlow<User?> = userFlow

    fun getRequestToken(
        userName: String,
        password: String,
        listener: RetrofitListener
    ) {
        uiScope.launch {
            safeApiCall(
                { userApiService.getToken() },
                success = { response ->
                    response.body()?.let {
                        requestToken = it.request_token
                        getAuthentication(userName, password, listener)
                        listener.onSuccess()
                    }
                },
                error = {
                    requestToken = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }

    private fun getAuthentication(
        userName: String,
        password: String,
        listener: RetrofitListener
    ) {
        val jsonObject = JSONObject()
        jsonObject.put("username", userName)
        jsonObject.put("password", password)
        jsonObject.put("request_token", requestToken)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody: RequestBody = jsonObject.toString().toRequestBody(mediaType)
        uiScope.launch {
            safeApiCall(
                { userApiService.authenticationWithLogin(requestBody) },
                success = { response ->
                    response.body()?.let {
                        requestToken = it.request_token
                        listener.onSuccess()
                        getSessionId()
                    }
                },
                error = {
                    requestToken = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }

    private fun getSessionId() {
        val jsonObject = JSONObject()
        jsonObject.put("request_token", requestToken)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody: RequestBody = jsonObject.toString().toRequestBody(mediaType)
        uiScope.launch {
            safeApiCall(
                { userApiService.newSession(requestBody) },
                success = { response ->
                    response.body()?.let { sessionResponse ->
                        sessionId = sessionResponse.session_id
                        sessionSuccessFlow.value = sessionResponse.success
                        sessionId?.let { storage.addPreferencesSession(it) }
                        getAccount()
                    }
                },
                error = {
                    sessionId = null
                }
            )
        }
    }

    fun getAccount() {
        uiScope.launch {
            safeApiCall(
                { userApiService.getAccount(storage.readPreferencesSession().toString()) },
                success = { response ->
                    response.body()?.let {
                        with(it) {
                            user.id = id
                            user.name = name
                            user.userName = username
                            user.sessionId = storage.readPreferencesSession().toString()
                            user.imageProfile = avatar.tmdb.avatar_path

                            storage.addPreferencesUserName(username)
                            storage.addPreferencesName(name)
                            storage.addPreferencesId(id.toString())
                            storage.addPreferencesImageProfile(avatar.tmdb.avatar_path)

                            userFlow.value = user
                        }
                    }
                },
                error = {
                }
            )
        }
    }

    fun markAsFavoriteMovie(
        id: Int,
        favorite: Boolean
    ) {
        val jsonObject = JSONObject()
        jsonObject.put("media_type", "movie")
        jsonObject.put("media_id", id)
        jsonObject.put("favorite", favorite)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody: RequestBody = jsonObject.toString().toRequestBody(mediaType)
        uiScope.launch {
            safeApiCall(
                {
                    userApiService.markAsFavorite(
                        user.id!!,
                        storage.readPreferencesSession()!!,
                        requestBody
                    )
                },
                success = {
                },
                error = {
                }
            )
        }
    }

    fun addWatchListMovie(
        id: Int,
        watchlist: Boolean
    ) {
        val jsonObject = JSONObject()
        jsonObject.put("media_type", "movie")
        jsonObject.put("media_id", id)
        jsonObject.put("watchlist", watchlist)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody: RequestBody = jsonObject.toString().toRequestBody(mediaType)
        uiScope.launch {
            safeApiCall(
                {
                    userApiService.addWatchList(
                        user.id!!,
                        storage.readPreferencesSession()!!,
                        requestBody
                    )
                },
                success = {
                },
                error = {
                }
            )
        }
    }

    fun addRatingFilm(
        id: Int,
        rating: Number
    ) {
        val jsonObject = JSONObject()
        jsonObject.put("value", rating)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody: RequestBody = jsonObject.toString().toRequestBody(mediaType)
        uiScope.launch {
            safeApiCall(
                {
                    userApiService.addRatingFilm(
                        id,
                        storage.readPreferencesSession()!!,
                        requestBody
                    )
                },
                success = {
                },
                error = {
                }
            )
        }
    }

    fun deleteRatingFilm(id: Int) {
        uiScope.launch {
            safeApiCall(
                {
                    storage.readPreferencesSession()?.let {
                        userApiService.deleteRatingFilm(
                            id,
                            it
                        )
                    }
                },
                success = {
                },
                error = {
                }
            )
        }
    }

    fun deleteSession() {
        storage.clearStorage()
        sessionSuccessFlow.value = false
    }
}
