package com.akshit.shgardi.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshit.shgardi.infra.network.NetworkResult
import com.akshit.shgardi.infra.repo.HomeRepository
import com.akshit.shgardi.models.PopularPersonListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _popularPersonListResponse: MutableLiveData<NetworkResult<PopularPersonListResponse>> = MutableLiveData()
    val popularPersonListResponse: LiveData<NetworkResult<PopularPersonListResponse>> = _popularPersonListResponse

    private val _searchPersonListResponse: MutableLiveData<NetworkResult<PopularPersonListResponse>> = MutableLiveData()
    val searchPersonListResponse: LiveData<NetworkResult<PopularPersonListResponse>> = _searchPersonListResponse



    fun getPopularPersonList(lang: String, pageNo: Int) = viewModelScope.launch {
        _popularPersonListResponse.value = NetworkResult.Loading()
        homeRepository.getPopularPersonList(lang, pageNo).collect { values ->
            _popularPersonListResponse.value = values
        }
    }

    fun searchPersonList(query: String, includeAdult: Boolean, lang: String, pageNo: Int) = viewModelScope.launch {
        _searchPersonListResponse.value = NetworkResult.Loading()
        homeRepository.searchPersonList(query, includeAdult, lang, pageNo).collect { values ->
            _searchPersonListResponse.value = values
        }
    }
}