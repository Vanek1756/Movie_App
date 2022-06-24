package com.example.movie.viewmodel

import androidx.lifecycle.ViewModel
import com.example.movie.model.repository.PeopleRepository
import com.example.movie.model.repository.SearchRepository
import com.example.movie.model.result.ResultPeople
import com.example.movie.model.search.MultiSearchApiResponse
import com.example.movie.util.RetrofitListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PeopleFragmentViewModel @Inject constructor(
    private val peopleRepository: PeopleRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val successRetrofitRequestFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    private val successErrorRequestFlow: MutableStateFlow<Throwable?> = MutableStateFlow(null)

    private val listener = object : RetrofitListener {
        override fun onSuccess() {
            successRetrofitRequestFlow.value = true
        }

        override fun onError(throwable: Throwable) {
            successErrorRequestFlow.value = throwable
        }
    }

    fun getSuccessRetrofitRequest(): StateFlow<Boolean?> = successRetrofitRequestFlow
    fun getErrorRetrofitRequest(): StateFlow<Throwable?> = successErrorRequestFlow
    fun getPeopleFlow(): StateFlow<List<ResultPeople>?> =
        peopleRepository.getPopularPeopleFlow()

    fun getDetailPeopleFlow(): StateFlow<ResultPeople?> = peopleRepository.getDetailsFlow()
    fun getSearchFlow(): StateFlow<MultiSearchApiResponse?> = searchRepository.getSearchFlow()

    fun updatePeopleFlow() = peopleRepository.getPopularPeople(listener)

    fun updateDetailsPeople(id: Int) = peopleRepository.getDetailPeople(id, listener)

    fun multiSearch(textSearch: String) = searchRepository.multiSearch(textSearch)

}