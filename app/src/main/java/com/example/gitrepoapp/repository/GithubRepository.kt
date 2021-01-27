package com.example.gitrepoapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.gitrepoapp.api.GithubApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepository @Inject constructor(private val githubApi: GithubApi) {

    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                    pageSize = 20,
                    maxSize = 100,
                    enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSource(githubApi, query) }
    ).liveData
}