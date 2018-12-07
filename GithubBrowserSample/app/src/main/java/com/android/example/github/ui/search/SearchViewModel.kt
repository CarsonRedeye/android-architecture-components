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

package com.android.example.github.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.github.domain.search.SearchReposInteractor
import com.android.example.github.testing.OpenForTesting
import com.android.example.github.vo.Repo
import com.android.example.github.vo.Result
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

@OpenForTesting
class SearchViewModel @Inject constructor(private val searchReposInteractor: SearchReposInteractor) : ViewModel() {

    private val disposables = CompositeDisposable()

    // Does anyone think it's worth having the private MutableLiveData and the public LiveData?
    // Surely code reviews will pick up if anyone is changing LiveData from the view.
    // I prefer to just expose the MutableLiveData (less boilerplate)
    private val _query = MutableLiveData<String>()
    val query: LiveData<String> = _query

    val results = MutableLiveData<Result<List<Repo>>>()

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == _query.value) {
            return
        }
        _query.value = input

        loadSearch(input)
    }

    fun refresh() {
        _query.value?.let {
            _query.value = it
        }
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    // This can possibly be cleaned up more with LiveDataReactiveStreams features.
    // Maybe move LiveData to interactor so we can just do: results = searchReposInteractor.search(query)
    private fun loadSearch(query: String) {
        results.value = Result.loading()
        disposables.add(searchReposInteractor.search(query).subscribe({
                                                                          results.postValue(Result.success(it))
                                                                      }, {
                                                                            results.postValue(Result.error("Some error"))
                                                                      }))
    }
}
