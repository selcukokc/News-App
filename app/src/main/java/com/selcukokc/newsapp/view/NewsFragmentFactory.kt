package com.selcukokc.newsapp.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.selcukokc.newsapp.adapter.FavoritesAdapter
import com.selcukokc.newsapp.adapter.NewsAdapter
import javax.inject.Inject

class NewsFragmentFactory @Inject constructor(
    private val glide : RequestManager,
    private val newsAdapter: NewsAdapter,
    private val favoritesAdapter: FavoritesAdapter
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            FeedFragment::class.java.name -> FeedFragment(newsAdapter)
            FavoritesFragment::class.java.name -> FavoritesFragment(favoritesAdapter)
            DetailFragment::class.java.name -> DetailFragment(glide)
            else -> super.instantiate(classLoader, className)
        }
    }







}