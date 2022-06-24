package com.example.movie.model.repository

import com.example.movie.model.result.ResultPeople
import com.example.movie.service.PeopleApiService
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
class PeopleRepository @Inject constructor(
    private val peopleApiService: PeopleApiService
) {
    private val uiScope = CoroutineScope(Dispatchers.Main)

    private var resultArrayList: List<ResultPeople>? = null
    private var resultDetails: ResultPeople? = null

    private val mutablePopularPeopleFlow: MutableStateFlow<List<ResultPeople>?> =
        MutableStateFlow(null)
    private val mutableDetailsPeopleFlow: MutableStateFlow<ResultPeople?> =
        MutableStateFlow(null)

    fun getDetailsFlow(): StateFlow<ResultPeople?> = mutableDetailsPeopleFlow
    fun getPopularPeopleFlow(): MutableStateFlow<List<ResultPeople>?> = mutablePopularPeopleFlow

    fun getDetailPeople(
        id: Int,
        listener: RetrofitListener
    ) {
        uiScope.launch {
            safeApiCall(
                { peopleApiService.getDetailPeople(id) },
                success = { response ->
                    response.body()?.let { resultDetails = it }
                    resultDetails?.let { mutableDetailsPeopleFlow.value = it }
                    listener.onSuccess()
                },
                error = {
                    resultDetails = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }

    fun getPopularPeople(listener: RetrofitListener) {
        uiScope.launch {
            safeApiCall(
                { peopleApiService.getPopularPeople() },
                success = { response ->
                    response.body()?.let { resultArrayList = it.results as ArrayList<ResultPeople> }
                    resultArrayList?.let { mutablePopularPeopleFlow.value = it }
                    listener.onSuccess()
                },
                error = {
                    resultArrayList = null
                    listener.onError(Throwable(it))
                }
            )
        }
    }
}
