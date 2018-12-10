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

package com.android.example.github.presentation.search


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.android.example.github.domain.model.Repo
import com.android.example.github.domain.model.Result
import com.android.example.github.domain.search.SearchReposInteractor
import com.android.example.github.util.mock
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class SearchViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()
    private val searchInteractor = mock(SearchReposInteractor::class.java)
    private lateinit var viewModel: SearchViewModel

    @Before
    fun init() {
        // need to init after instant executor rule is established.
        viewModel = SearchViewModel(searchInteractor)
        `when`(searchInteractor.search(ArgumentMatchers.anyString())).thenReturn(mock())
    }

    @Test
    fun basic() {
        val result = mock<Observer<Result<List<Repo>>>>()
        viewModel.results.observeForever(result)
        viewModel.setQuery("foo")
        verify(searchInteractor).search("foo")
    }

    @Test
    fun refresh() {
        viewModel.refresh()
        verifyNoMoreInteractions(searchInteractor)

        viewModel.setQuery("foo")
        viewModel.refresh()
        verify(searchInteractor).search("foo")
    }

    @Test
    fun resetSameQuery() {
        viewModel.setQuery("foo")
        verify(searchInteractor).search("foo")

        reset(searchInteractor)
        `when`(searchInteractor.search(ArgumentMatchers.anyString())).thenReturn(mock())
        viewModel.setQuery("FOO")
        verifyNoMoreInteractions(searchInteractor)
        viewModel.setQuery("bar")
        verify(searchInteractor).search("bar")
    }
}