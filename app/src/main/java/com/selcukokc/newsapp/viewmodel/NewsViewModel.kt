package com.selcukokc.newsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selcukokc.newsapp.model.Article
import com.selcukokc.newsapp.model.NewsResponse
import com.selcukokc.newsapp.repo.NewsRepo
import com.selcukokc.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository : NewsRepo
) : ViewModel() {

    val favoriteNews = repository.getFavoriteNews()

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null
    var newSearchQuery:String? = null
    var oldSearchQuery:String? = null


    //ROOM

    fun insertNews(article: Article) = viewModelScope.launch {
        repository.insertNews(article)
    }

    fun deleteNews(article: Article) = viewModelScope.launch {
        repository.deleteNews(article)
    }


    //API

    fun searchNews(query: String) = viewModelScope.launch {
        safeSearchNewsCall(query)
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        newSearchQuery = searchQuery
        searchNews.postValue(Resource.loading(null))
        try {
            val response = repository.searchNews(searchNewsPage, searchQuery)
            searchNews.postValue(handleSearchNewsResponse(response))

        } catch(t: Throwable) {
            when(t) {
                is IOException -> searchNews.postValue(Resource.error("Network Failure",null))
                else -> searchNews.postValue(Resource.error("Conversion Error",null))
            }
        }
    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if(searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.error(response.message(),null)
    }






}