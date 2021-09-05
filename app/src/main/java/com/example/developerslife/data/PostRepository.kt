package com.example.developerslife.data

import com.example.developerslife.data.remote.RemoteDataSource

// Need to add the logic for working with the local database
// Now just remote source
class PostRepository(private val remoteDataSource: RemoteDataSource) {

    fun getRandomResult() = remoteDataSource.getRandomResult()

    fun getLatestResult(pageNumber: Int, pageSize: Int) = remoteDataSource.getLatestResult(pageNumber, pageSize)

    fun getTopResult(pageNumber: Int, pageSize: Int) = remoteDataSource.getTopResult(pageNumber, pageSize)

}