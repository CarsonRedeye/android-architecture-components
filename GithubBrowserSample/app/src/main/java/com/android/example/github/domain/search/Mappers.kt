package com.android.example.github.domain.search

import com.android.example.github.api.RepoSearchResponse
import com.android.example.github.repository.DomainException
import com.android.example.github.vo.Repo

fun mapRepoSearchResponseToRepos(repoSearchResponse: RepoSearchResponse): List<Repo> {
    if (repoSearchResponse.someImportantThing == null) {
        //throw DomainException.MalformedResponse()
    }
    return repoSearchResponse.items
}