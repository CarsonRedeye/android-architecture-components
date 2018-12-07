package com.android.example.github.domain.search

import com.android.example.github.data.github.RepoService
import com.android.example.github.testing.OpenForTesting
import com.android.example.github.domain.model.Repo
import io.reactivex.Single
import javax.inject.Inject

@OpenForTesting
class SearchReposInteractor @Inject constructor(private val repoService: RepoService) {

    fun search(query: String): Single<List<Repo>> {
        return repoService.search(query)
    }
}