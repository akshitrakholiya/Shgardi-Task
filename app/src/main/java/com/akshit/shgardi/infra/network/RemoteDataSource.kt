package com.akshit.shgardi.infra.network

import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: WebApiInterface) {
    suspend fun getPopularPersonList(lang: String,pageNo: Int) =
        api.getPopularPersonList(WebApiInterface.BASE_URL+WebApiInterface.API_VERSION+"person/popular?language=${lang}&page=${pageNo}")

    suspend fun getPersonInfo(lang: String,personId: Int) =
        api.getPersonInfo(WebApiInterface.BASE_URL+WebApiInterface.API_VERSION+"person/${personId}?language=${lang}")

    suspend fun getPersonImages(personId: Int) =
        api.getPersonImages(WebApiInterface.BASE_URL+WebApiInterface.API_VERSION+"person/${personId}/images")
}