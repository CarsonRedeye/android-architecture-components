package com.android.example.github.domain.search

import com.android.example.github.data.github.model.RepoSearchResponse
import com.android.example.github.domain.model.Repo

fun mapRepoSearchResponseToRepos(repoSearchResponse: RepoSearchResponse): List<Repo> {
    if (repoSearchResponse.someImportantThing == null) {
        //throw DomainException.MalformedResponse()
    }
    return repoSearchResponse.items
}