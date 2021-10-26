package com.selcukokc.newsapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import com.selcukokc.newsapp.R
import com.selcukokc.newsapp.databinding.FragmentDetailBinding
import com.selcukokc.newsapp.databinding.FragmentSourceBinding
import com.selcukokc.newsapp.model.Article

class SourceFragment : Fragment(R.layout.fragment_source) {

    private var fragmentBinding : FragmentSourceBinding? = null
    lateinit var navController : NavController
    val args: SourceFragmentArgs by navArgs()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = args.newsUrl

        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        val binding = FragmentSourceBinding.bind(view)
        fragmentBinding = binding

        binding.webView.settings.javaScriptEnabled = true

        url?.let {
            binding.webView.webViewClient = WebViewClient()
            binding.webView.loadUrl(url)
        }

        binding.sourceToolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }



    }
}