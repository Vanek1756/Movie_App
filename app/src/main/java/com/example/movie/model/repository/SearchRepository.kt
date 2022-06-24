package com.example.movie.model.repository

import com.example.movie.model.search.MultiSearchApiResponse
import com.example.movie.service.SearchApiService
import com.example.movie.util.safeApiCall
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchRepository @Inject constructor(
    private val searchApiService: SearchApiService
) {
    private val uiScope = CoroutineScope(Dispatchers.Main)

    private var resultSearch: MultiSearchApiResponse? = null

    private val mutableFlow: MutableStateFlow<MultiSearchApiResponse?> = MutableStateFlow(null)

    fun getSearchFlow(): StateFlow<MultiSearchApiResponse?> = mutableFlow

    fun multiSearch(text: String) {
        uiScope.launch {
            safeApiCall(
                { searchApiService.multiSearch(text) },
                success = { response ->
                    response.body()?.let { resultSearch = it }
                    resultSearch?.let { mutableFlow.value = it }
                },
                error = {
                    resultSearch = null
                }
            )
        }
    }
}
