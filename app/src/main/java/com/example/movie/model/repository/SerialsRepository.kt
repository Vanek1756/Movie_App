package com.example.movie.model.repository

import com.example.movie.model.result.ResultSerials
import com.example.movie.service.SerialsApiService
import com.example.movie.util.RetrofitListener
import com.example.movie.util.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Singleton
class SerialsRepository @Inject constructor(
    private val movieApiService: SerialsApiService
) {
    private val uiScope = CoroutineScope(Dispatchers.Main)

    private var resultPopular: ArrayList<ResultSerials>? = null
    private var resultOnAir: ArrayList<ResultSerials>? = null
    private var resultOnTv: ArrayList<ResultSerials>? = null
    private var resultTheBest: ArrayList<ResultSerials>? = null
    private var resultDetail: ResultSerials? = null
    private val mutablePopularFlow: MutableStateFlow<List<ResultSerials>?> = MutableStateFlow(null)
    private val mutableOnAirFlow: MutableStateFlow<List<ResultSerials>?> = MutableStateFlow(null)
    private val mutableOnTvFlow: MutableStateFlow<List<ResultSerials>?> = MutableStateFlow(null)
    private val mutableTheBestFlow: MutableStateFlow<List<ResultSerials>?> = MutableStateFlow(null)
    private val mutableDetailsFlow: MutableStateFlow<ResultSerials?> = MutableStateFlow(null)

    fun getPopularFlow(): StateFlow<List<ResultSerials>?> = mutablePopularFlow
    fun getOnAirFlow(): StateFlow<List<ResultSerials>?> = mutableOnAirFlow
    fun getOnTvFlow(): StateFlow<List<ResultSerials>?> = mutableOnTvFlow
    fun getTheBestFlow(): StateFlow<List<ResultSerials>?> = mutableTheBestFlow
    fun getDetailsFlow(): StateFlow<ResultSerials?> = mutableDetailsFlow

    fun getPopularSerials(listener: RetrofitListener) {
        uiScope.launch {
            safeApiCall(
                { movieApiService.getPopularSerials() },
                success = { response ->
                    response.body()?.let { resultPopular = it.results as ArrayList<ResultSerials> }
                    resultPopular?.let {
                        mutablePopularFlow.value = it
                        listener.onSuccess()
                    }
                },
                error = {
                    resultPopular = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }

    fun getDetailsSerials(
        id: Int,
        listener: RetrofitListener
    ) {
        uiScope.launch {
            safeApiCall(
                { movieApiService.getDetailsSerials(id) },
                success = { response ->
                    response.body()?.let { resultDetail = it }
                    resultDetail?.let {
                        mutableDetailsFlow.value = it
                        listener.onSuccess()
                    }
                },
                error = {
                    resultDetail = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }

    fun getOnAirSerials(listener: RetrofitListener) {
        uiScope.launch {
            safeApiCall(
                { movieApiService.getOnAirSerials() },
                success = { response ->
                    response.body()?.let { resultOnAir = it.results as ArrayList<ResultSerials> }
                    resultOnAir?.let {
                        mutableOnAirFlow.value = it
                        listener.onSuccess()
                    }
                },
                error = {
                    resultOnAir = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }

    fun getTvSerials(listener: RetrofitListener) {
        uiScope.launch {
            safeApiCall(
                { movieApiService.getTvSerials() },
                success = { response ->
                    response.body()?.let { resultOnTv = it.results as ArrayList<ResultSerials> }
                    resultOnTv?.let {
                        mutableOnTvFlow.value = it
                        listener.onSuccess()
                    }
                },
                error = {
                    resultOnTv = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }

    fun getTheBestSerials(listener: RetrofitListener) {
        uiScope.launch {
            safeApiCall(
                { movieApiService.getTheBestSerials() },
                success = { response ->
                    response.body()?.let { resultTheBest = it.results as ArrayList<ResultSerials> }
                    resultTheBest?.let {
                        mutableTheBestFlow.value = it
                        listener.onSuccess()
                    }
                },
                error = {
                    resultTheBest = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }
}
