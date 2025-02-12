package com.akshit.shgardi.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshit.shgardi.infra.network.NetworkResult
import com.akshit.shgardi.infra.repo.HomeRepository
import com.akshit.shgardi.models.PersonImagesResponse
import com.akshit.shgardi.models.PersonInfoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonInfoViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _personInfoResponse: MutableLiveData<NetworkResult<PersonInfoResponse>> = MutableLiveData()
    val personInfoResponse: LiveData<NetworkResult<PersonInfoResponse>> = _personInfoResponse


    private val _personImagesResponse: MutableLiveData<NetworkResult<PersonImagesResponse>> = MutableLiveData()
    val personImagesResponse: LiveData<NetworkResult<PersonImagesResponse>> = _personImagesResponse



    fun getPersonInfo(lang: String, personId: Int) = viewModelScope.launch {
        _personInfoResponse.value = NetworkResult.Loading()
        homeRepository.getPersonInfo(lang, personId).collect { values ->
            _personInfoResponse.value = values
        }
    }

    fun getPersonImages(personId: Int) = viewModelScope.launch {
        _personInfoResponse.value = NetworkResult.Loading()
        homeRepository.getPersonImages(personId).collect { values ->
            _personImagesResponse.value = values
        }
    }
}