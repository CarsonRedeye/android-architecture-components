package com.android.example.github.domain.search

import com.android.example.github.repository.RepoRepositoryRx
import com.android.example.github.testing.OpenForTesting
import com.android.example.github.vo.Repo
import io.reactivex.Single
import javax.inject.Inject

@OpenForTesting
class SearchReposInteractor @Inject constructor(private val repoRepositoryRx: RepoRepositoryRx) {

    fun search(query: String): Single<List<Repo>> {
        return repoRepositoryRx.search(query)
    }
}