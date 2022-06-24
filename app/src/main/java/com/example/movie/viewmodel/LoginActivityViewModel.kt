package com.example.movie.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.movie.model.User
import com.example.movie.model.repository.UserRepository
import com.example.movie.util.RetrofitListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel @Inject constructor(
    application: Application,
    private val userRepository: UserRepository,
) : AndroidViewModel(application) {

    private var isUserCreate: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    private var userForgot: MutableStateFlow<User?> = MutableStateFlow(null)
    private val successRetrofitRequestFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    private val errorRetrofitRequestFlow: MutableStateFlow<Throwable?> = MutableStateFlow(null)

    private val listener = object : RetrofitListener {
        override fun onSuccess() {
            successRetrofitRequestFlow.value = true
            errorRetrofitRequestFlow.value = null
        }

        override fun onError(throwable: Throwable) {
            errorRetrofitRequestFlow.value = throwable
            successRetrofitRequestFlow.value = null
        }
    }

    fun getSuccessRetrofitRequest(): StateFlow<Boolean?> = successRetrofitRequestFlow
    fun getErrorRetrofitRequest(): StateFlow<Throwable?> = errorRetrofitRequestFlow
    fun getSessionSuccessFlow(): StateFlow<Boolean?> = userRepository.getSessionSuccessFlow()
    fun getIsUserCreateFlow(): StateFlow<Boolean?> = isUserCreate
    fun getUserForgotFlow(): StateFlow<User?> = userForgot

    fun authenticationSession(userName: String, password: String) =
        userRepository.getRequestToken(userName, password, listener)

}