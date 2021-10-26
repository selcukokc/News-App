package com.selcukokc.newsapp.repo

import androidx.lifecycle.LiveData
import com.selcukokc.newsapp.api.NewsAPI
import com.selcukokc.newsapp.db.NewsDao
import com.selcukokc.newsapp.model.Article

import javax.inject.Inject

class NewsRepo @Inject constructor(
    private val newsDao: NewsDao,
    private val newsAPI: NewsAPI
) {

    suspend fun insertNews(article: Article) {
        newsDao.insertArticle(article)
    }

    suspend fun deleteNews(article: Article) {
        newsDao.deleteArticle(article)
    }

    fun getFavoriteNews(): LiveData<List<Article>> {
        return newsDao.getArticles()
    }

    suspend fun searchNews(pageNumber: Int, query: String) = newsAPI.searchNews(pageNumber, query)



}