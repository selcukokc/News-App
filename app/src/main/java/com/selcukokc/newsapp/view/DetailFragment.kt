package com.selcukokc.newsapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.selcukokc.newsapp.R
import com.selcukokc.newsapp.databinding.FragmentDetailBinding
import com.selcukokc.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment @Inject constructor(
    val glide: RequestManager
) : Fragment(R.layout.fragment_detail) {

    private var fragmentBinding : FragmentDetailBinding? = null
    lateinit var newsViewModel : NewsViewModel
    lateinit var navController : NavController
    val args: DetailFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        val article = args.article

        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        val binding = FragmentDetailBinding.bind(view)
        fragmentBinding = binding

        binding.btnSource.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToSourceFragment(article.url))
        }

        binding.authorName.text = article.author
        binding.newsDate.text = article.publishedAt.toString().substring(0,10)
        binding.newsTitle.text = article.title
        binding.newsBody.text = article.description
        glide.load(article.urlToImage).into(binding.newsImage)


        binding.detailToolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.detailToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.itemFavorite -> {
                    newsViewModel.insertNews(article)
                    Snackbar.make(view, "Successfully added article", Snackbar.LENGTH_LONG).apply {
                        show()
                    }
                }
                R.id.itemShare -> {
                    val i = Intent(Intent.ACTION_SEND)
                    i.type = "text/plain"
                    i.putExtra(Intent.EXTRA_SUBJECT, "Sharing Article")
                    i.putExtra(Intent.EXTRA_TEXT, article.url)
                    startActivity(Intent.createChooser(i, "Share Article"))
                }
            }
            true

        }


    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }

}