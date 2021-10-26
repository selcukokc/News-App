package com.selcukokc.newsapp.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.selcukokc.newsapp.R
import com.selcukokc.newsapp.api.NewsAPI
import com.selcukokc.newsapp.db.NewsDao
import com.selcukokc.newsapp.db.NewsDatabase
import com.selcukokc.newsapp.repo.NewsRepo
import com.selcukokc.newsapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context,NewsDatabase::class.java,"NewsDatabase").build()

    @Singleton
    @Provides
    fun injectDao(database: NewsDatabase) = database.newsDao()

    @Singleton
    @Provides
    fun injectNewsAPI() : NewsAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL).build().create(NewsAPI::class.java)
    }

    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) = Glide
        .with(context).setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_baseline_wallpaper_24)
                .error(R.drawable.ic_baseline_wallpaper_24)
        )

}