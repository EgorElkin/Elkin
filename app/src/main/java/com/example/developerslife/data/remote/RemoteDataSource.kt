package com.example.developerslife.data.remote

import com.example.developerslife.data.remote.network.RetrofitBuilder
import com.example.developerslife.model.Result
import com.example.developerslife.model.Post
import com.example.developerslife.model.SeveralPosts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class RemoteDataSource {

    private val flowApiService = RetrofitBuilder.apiService

    private suspend fun <T> getResponse(request: suspend () -> Response<T>): Result<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                Result.success(result.body())
            } else {
                Result.error("Loading failed!")
            }
        } catch (e: Throwable) {
            Result.error("Unknown Error: ${e.message}")
        }
    }

    fun getRandomResult(): Flow<Result<Post>> {
        return flow {
            emit(Result.loading())
            val response = getResponse { flowApiService.fetchRandomResponse() }
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    fun getLatestResult(pageNumber: Int, pageSize: Int): Flow<Result<SeveralPosts>>{
        return flow {
            emit(Result.loading())
            val response = getResponse { flowApiService.fetchLatestPosts(pageNumber.toString(), pageSize.toString()) }
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    fun getTopResult(pageNumber: Int, pageSize: Int): Flow<Result<SeveralPosts>> {
        return flow {
            emit(Result.loading())
            val response = getResponse { flowApiService.fetchTopPosts(pageNumber.toString(), pageSize.toString()) }
            emit(response)
        }.flowOn(Dispatchers.IO)
    }
}