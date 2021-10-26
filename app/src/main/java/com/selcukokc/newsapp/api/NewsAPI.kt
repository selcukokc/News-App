package com.selcukokc.newsapp.api

import com.selcukokc.newsapp.model.NewsResponse
import com.selcukokc.newsapp.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("page") pageNumber: Int = 1,
        @Query("q") query: String,
        @Query("apiKey") apikey: String = API_KEY
    ) : Response<NewsResponse>

}