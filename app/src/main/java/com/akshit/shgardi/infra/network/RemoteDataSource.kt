package com.akshit.shgardi.infra.network

import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: WebApiInterface) {
    suspend fun getPopularPersonList(lang: String,pageNo: Int) =
        api.getPopularPersonList(WebApiInterface.BASE_URL+WebApiInterface.API_VERSION+"person/popular?language=${lang}&page=${pageNo}")
}