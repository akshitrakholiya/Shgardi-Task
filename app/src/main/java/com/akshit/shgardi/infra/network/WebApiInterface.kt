package com.akshit.shgardi.infra.network

import com.akshit.shgardi.models.PopularPersonListResponse
import retrofit2.Response
import retrofit2.http.*

interface WebApiInterface {

    @GET()
    suspend fun getPopularPersonList(@Url url: String): Response<PopularPersonListResponse>

    companion object {

        //Mock API
        var BASE_URL = "https://api.themoviedb.org/"
        val API_VERSION = "3/"

        val PERSON_IMG_PREFIX = "https://image.tmdb.org/t/p/w500"

    }
}