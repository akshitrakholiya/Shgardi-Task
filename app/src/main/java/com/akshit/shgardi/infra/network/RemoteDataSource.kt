package com.akshit.shgardi.infra.network

import com.akshit.shgardi.models.UserInfoRequest
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: WebApiInterface) {
    suspend fun getUserInfo(userInfoRequest: UserInfoRequest) =
        api.getUserInfo(userInfoRequest,WebApiInterface.BASE_URL+"endpoints/")
}