package com.selcukokc.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.selcukokc.newsapp.R
import com.selcukokc.newsapp.databinding.NewsItemBinding
import com.selcukokc.newsapp.model.Article
import javax.inject.Inject

class NewsAdapter @Inject constructor(
    val glide : RequestManager
) : RecyclerView.Adapter<NewsAdapter.ItemViewHolder>(){

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = NewsItemBinding.bind(view)
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var news: List<Article>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item,null,false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val article = recyclerListDiffer.currentList[position]
        with(holder){
            binding.newsTitle.text = article.title
            binding.newsBody.text = article.content
            glide.load(article.urlToImage).into(binding.newsImage)

            binding.cardView.setOnClickListener {
                onItemClickListener?.let { it(article) }
            }

        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return recyclerListDiffer.currentList.size
    }


}