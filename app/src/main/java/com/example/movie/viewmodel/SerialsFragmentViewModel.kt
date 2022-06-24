package com.example.movie.viewmodel

import androidx.lifecycle.ViewModel
import com.example.movie.model.repository.SerialsRepository
import com.example.movie.model.result.ResultSerials
import com.example.movie.util.RetrofitListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SerialsFragmentViewModel @Inject constructor(
    private val serialsRepository: SerialsRepository
) : ViewModel() {

    private val successRetrofitRequestFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    private val successErrorRequestFlow: MutableStateFlow<Throwable?> = MutableStateFlow(null)

    private val listener = object : RetrofitListener {
        override fun onSuccess() {
            successRetrofitRequestFlow.value = true
            successErrorRequestFlow.value = null
        }

        override fun onError(throwable: Throwable) {
            successErrorRequestFlow.value = throwable
            successRetrofitRequestFlow.value = null
        }
    }

    fun getTvSerialsFlow(): StateFlow<List<ResultSerials>?> = serialsRepository.getOnTvFlow()
    fun getSuccessRetrofitRequest(): StateFlow<Boolean?> = successRetrofitRequestFlow
    fun getErrorRetrofitRequest(): StateFlow<Throwable?> = successErrorRequestFlow
    fun getPopularSerialsFlow(): StateFlow<List<ResultSerials>?> =
        serialsRepository.getPopularFlow()

    fun getOnAirSerialsFlow(): StateFlow<List<ResultSerials>?> =
        serialsRepository.getOnAirFlow()

    fun getBestSerialsFlow(): StateFlow<List<ResultSerials>?> =
        serialsRepository.getTheBestFlow()

    fun updatePopularSerialsFlow() = serialsRepository.getPopularSerials(listener)

    fun updateOnAirSerialsFlow() = serialsRepository.getOnAirSerials(listener)

    fun updateTvSerialsFlow() = serialsRepository.getTvSerials(listener)

    fun updateBestSerialsFlow() = serialsRepository.getTheBestSerials(listener)

}