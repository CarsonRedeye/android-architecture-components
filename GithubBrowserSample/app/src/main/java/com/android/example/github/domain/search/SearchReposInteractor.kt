package com.android.example.github.domain.search

import com.android.example.github.data.github.SearchRepository
import com.android.example.github.testing.OpenForTesting
import com.android.example.github.domain.model.Repo
import io.reactivex.Single
import javax.inject.Inject

/**
 * Interactors are for combining or transforming data sources
 */
@OpenForTesting
class SearchReposInteractor @Inject constructor(private val searchRepository: SearchRepository) {

    fun search(query: String): Single<List<Repo>> {
        return searchRepository.search(query)
    }
}