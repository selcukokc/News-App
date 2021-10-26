package com.selcukokc.newsapp.view

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.selcukokc.newsapp.R
import com.selcukokc.newsapp.adapter.NewsAdapter
import com.selcukokc.newsapp.databinding.FragmentFeedBinding
import com.selcukokc.newsapp.util.Constants
import com.selcukokc.newsapp.util.Constants.DELAY
import com.selcukokc.newsapp.util.Status
import com.selcukokc.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment @Inject constructor(
    val newsAdapter: NewsAdapter,
) : Fragment(R.layout.fragment_feed) {

    lateinit var binding : FragmentFeedBinding
    private var fragmentBinding : FragmentFeedBinding? = null
    lateinit var newsViewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as MainActivity).newsViewModel

        binding = FragmentFeedBinding.bind(view)
        fragmentBinding = binding

        binding.newsRecyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(this@FeedFragment.scrollListener)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }


        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_feedFragment_to_detailFragment,
                bundle
            )
        }

        observeEditTextQuery()

        binding.btnDeleteQuery.setOnClickListener {
            binding.searchEt.setText("")
        }

        observeNews()

    }




    private fun observeEditTextQuery() {
        var job: Job? = null
        binding.searchEt.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        newsViewModel.searchNews(editable.toString())
                    }
                }
            }
        }
    }



    private fun observeNews() {
        newsViewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.recyclerListDiffer.submitList(newsResponse.articles.toList())
                    }
                }
                Status.ERROR-> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "Error: $message", Toast.LENGTH_LONG).show()
                    }
                }

                Status.LOADING -> {
                    showProgressBar()
                }
            }
        })
    }


    private fun hideProgressBar() {
        binding.newsProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.newsProgressBar.visibility = View.VISIBLE
        isLoading = true
    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false


    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                newsViewModel.searchNews(binding.searchEt.text.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }



    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }



}