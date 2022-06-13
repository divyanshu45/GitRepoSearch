package com.example.gitrepoapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.paging.LoadState
import com.example.gitrepoapp.R
import com.example.gitrepoapp.adapter.RepoAdapter
import com.example.gitrepoapp.adapter.RepoLoadStateAdapter
import com.example.gitrepoapp.databinding.FragmentSearchBinding
import com.example.gitrepoapp.ui.RepoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel by viewModels<RepoViewModel>()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        _binding = FragmentSearchBinding.bind(view)
        val adapter = RepoAdapter()

        binding.apply {
            recycler.apply {
                setHasFixedSize(true)
                itemAnimator = null
                this.adapter = adapter.withLoadStateHeaderAndFooter(
                    header = RepoLoadStateAdapter { adapter.retry() },
                    footer = RepoLoadStateAdapter { adapter.retry() }
                )
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }

            btnRetry.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.repos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progress.isVisible = loadState.source.refresh is LoadState.Loading
                recycler.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                error.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recycler.isVisible = false
                    emptyTv.isVisible = true
                } else {
                    emptyTv.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}