/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example.github.data.github

import com.android.example.github.data.util.NetworkHandler
import com.android.example.github.data.util.mapErrors
import com.android.example.github.data.util.threadForNetwork
import com.android.example.github.domain.DomainException
import com.android.example.github.domain.model.Repo
import com.android.example.github.domain.search.mapRepoSearchResponseToRepos
import com.android.example.github.testing.OpenForTesting
import io.reactivex.Single
import org.threeten.bp.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Here is for deciding where to get data, from network/memory/disk
 */
@Singleton
@OpenForTesting
class SearchRepository @Inject constructor(private val gitHubEndpoints: GitHubEndpoints,
                                           private val networkHandler: NetworkHandler) {

    // We can cache things in memory here if we like. Eg:
    var serversNeedToSleep: Boolean = true

    fun search(query: String): Single<List<Repo>> {
        if (networkHandler.isConnected == false || networkHandler.isConnected == null) {
            return Single.error(DomainException.NoNetwork())
        }

        return if (serversNeedToSleep && LocalTime.now().isAfter(LocalTime.MIDNIGHT)) {
            Single.error(DomainException.ItsTooLateAtNight())
        } else {
            gitHubEndpoints.searchReposRx(query)
                    .threadForNetwork()
                    .map { mapRepoSearchResponseToRepos(it) }
                    .mapErrors()
        }
    }

    // We can also decide to get things from DB or from network. Eg:

    /*fun search(query: String): Single<List<Repo>> {
        lastCalled?.let {
            if (System.currentTimeMillis() - it > DateUtils.HOUR_IN_MILLIS) {
                lastCalled = System.currentTimeMillis()
                return gitHubEndpoints.searchReposRx(query)
                        .threadForNetwork()
                        .map { mapRepoSearchResponseToRepos(it) }
                        .mapErrors()
            } else {
                return db.getRepos()
            }
        }
    }*/
}