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

package com.android.example.github.repository

import com.android.example.github.api.GithubService
import com.android.example.github.api.RepoSearchResponse
import com.android.example.github.data.util.mapErrors
import com.android.example.github.data.util.threadForNetwork
import com.android.example.github.domain.search.mapRepoSearchResponseToRepos
import com.android.example.github.testing.OpenForTesting
import com.android.example.github.vo.Repo
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles Repo instances.
 *
 * unfortunate naming :/ .
 * Repo - value object name
 * Repository - type of this class.
 */
@Singleton
@OpenForTesting
class RepoRepositoryRx @Inject constructor(private val githubService: GithubService) {

    // This will get
    fun search(query: String): Single<List<Repo>> {
        return githubService.searchReposRx(query)
                .threadForNetwork()
                .map { mapRepoSearchResponseToRepos(it) }
                .mapErrors()
    }
}