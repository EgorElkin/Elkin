package com.example.developerslife.data.remote.network

import com.example.developerslife.model.Post
import com.example.developerslife.model.SeveralPosts
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("random?json=true")
    suspend fun fetchRandomResponse(): Response<Post>

    //https://developerslife.ru/latest/0?pageSize=5&json=true
    @GET("latest/{pageNumber}?json=true")
    suspend fun fetchLatestPosts(
        @Path("pageNumber") pageNumber: String = "0",
        @Query("pageSize") perPage: String = "5"
    ): Response<SeveralPosts>

    //https://developerslife.ru/top/0?pageSize=5&json=true
    @GET("top/{pageNumber}?json=true")
    suspend fun fetchTopPosts(
        @Path("pageNumber") pageNumber: String = "0",
        @Query("pageSize") perPage: String = "5"
    ): Response<SeveralPosts>

}