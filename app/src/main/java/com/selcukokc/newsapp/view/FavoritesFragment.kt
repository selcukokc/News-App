package com.selcukokc.newsapp.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.selcukokc.newsapp.R
import com.selcukokc.newsapp.adapter.FavoritesAdapter
import com.selcukokc.newsapp.adapter.NewsAdapter
import com.selcukokc.newsapp.databinding.FragmentFavoritesBinding
import com.selcukokc.newsapp.databinding.FragmentFeedBinding
import com.selcukokc.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment @Inject constructor(
    val favoritesAdapter: FavoritesAdapter,
) : Fragment(R.layout.fragment_favorites) {

    lateinit var binding : FragmentFavoritesBinding
    private var fragmentBinding : FragmentFavoritesBinding? = null
    lateinit var newsViewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as MainActivity).newsViewModel

        val binding = FragmentFavoritesBinding.bind(view)
        fragmentBinding = binding

        binding.favoriteNewsRV.apply {
            adapter = favoritesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        favoritesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }

            findNavController().navigate(R.id.action_navigation_favorites_to_detailFragment, bundle)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = favoritesAdapter.recyclerListDiffer.currentList[position]
                newsViewModel.deleteNews(article)
                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.favoriteNewsRV)
        }

        newsViewModel.favoriteNews.observe(viewLifecycleOwner, Observer { articles ->
            favoritesAdapter.recyclerListDiffer.submitList(articles)
        })

    }




    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }


}