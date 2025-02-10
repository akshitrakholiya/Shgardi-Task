package com.akshit.shgardi.infra.network

import com.akshit.shgardi.models.UserInfoRequest
import com.akshit.shgardi.models.UserInfoResponse
import retrofit2.Response
import retrofit2.http.*

interface WebApiInterface {

    @POST()
    suspend fun getUserInfo(@Body userInfoRequest: UserInfoRequest, @Url url: String): Response<UserInfoResponse>

    companion object {

        //Mock API
        var BASE_URL = "https://domain.com/"
    }
}