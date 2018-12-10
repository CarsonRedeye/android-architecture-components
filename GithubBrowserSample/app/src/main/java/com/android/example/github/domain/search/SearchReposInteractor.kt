package com.android.example.github.domain.search

import com.android.example.github.data.github.SearchRepository
import com.android.example.github.data.github.TimeOfDayRepository
import com.android.example.github.domain.DomainException
import com.android.example.github.testing.OpenForTesting
import com.android.example.github.domain.model.Repo
import io.reactivex.Single
import javax.inject.Inject

/**
 * Interactors are for combining or transforming data from repositories
 */
@OpenForTesting
class SearchReposInteractor @Inject constructor(private val searchRepository: SearchRepository,
                                                private val timeOfDayRepository: TimeOfDayRepository) {

    fun search(query: String): Single<List<Repo>> {
        return if (timeOfDayRepository.isSleepingTime()) {
            Single.error(DomainException.ItsTooLateAtNight())
        } else {
            searchRepository.search(query)
        }
    }
}