package com.akshit.shgardi.infra.repo

import com.akshit.shgardi.infra.network.BaseApiResponse
import com.akshit.shgardi.infra.network.NetworkResult
import com.akshit.shgardi.infra.network.RemoteDataSource
import com.akshit.shgardi.models.PersonInfoResponse
import com.akshit.shgardi.models.PopularPersonListResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class HomeRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun getPopularPersonList(lang:String, pageNo: Int): Flow<NetworkResult<PopularPersonListResponse>> {
        return flow<NetworkResult<PopularPersonListResponse>> {
            emit(safeApiCall { remoteDataSource.getPopularPersonList(lang, pageNo) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getPersonInfo(lang:String, personId: Int): Flow<NetworkResult<PersonInfoResponse>> {
        return flow<NetworkResult<PersonInfoResponse>> {
            emit(safeApiCall { remoteDataSource.getPersonInfo(lang, personId) })
        }.flowOn(Dispatchers.IO)
    }

}