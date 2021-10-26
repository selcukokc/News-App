package com.selcukokc.newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.selcukokc.newsapp.model.Article

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("SELECT* FROM articles")
    fun getArticles() : LiveData<List<Article>>

}