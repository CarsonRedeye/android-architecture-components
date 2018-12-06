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

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.os.IBinder
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.android.example.github.AppExecutors
import com.android.example.github.R
import com.android.example.github.di.Injectable
import com.android.example.github.vo.Status
import com.android.example.github.testing.OpenForTesting
import com.android.example.github.ui.common.RepoListAdapter
import com.android.example.github.ui.util.observe
import com.android.example.github.util.autoCleared
import kotlinx.android.synthetic.main.loading_state.*
import kotlinx.android.synthetic.main.search_fragment.*
import javax.inject.Inject

@OpenForTesting
class SearchFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var adapter by autoCleared<RepoListAdapter>()

    lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SearchViewModel::class.java)
        initRecyclerView()
        val rvAdapter = RepoListAdapter { repo ->
            // This should probably be handled by viewModel, for easier testing?
            navController().navigate(
                    SearchFragmentDirections.showRepo(repo.owner.login, repo.name)
            )
        }
        repo_list.adapter = rvAdapter
        adapter = rvAdapter

        initSearchInputListener()

        retry.setOnClickListener { searchViewModel.refresh() }
        subscribeToViewModel(searchViewModel)
    }

    fun subscribeToViewModel(searchViewModel: SearchViewModel) {

        observe(searchViewModel.results) {
            adapter.submitList(it?.data)

            // Traditional if/else visible/gone
            if (it?.status == Status.SUCCESS && it.data?.isEmpty() == true) {
                no_results_text.visibility = View.VISIBLE
            } else {
                no_results_text.visibility = View.GONE
            }
            // Possible style to shorten the visible/gone switching using android-ktx extensions
            // (I think I prefer this to help not to forget to hide)
            progress_bar.isVisible = it?.status == Status.LOADING

            if (it?.status == Status.ERROR) {
                retry.visibility = View.VISIBLE
                error_msg.text = it.message ?: getString(R.string.unknown_error)
            } else {
                retry.visibility = View.GONE
                error_msg.visibility = View.GONE
            }


        }

        observe(searchViewModel.query) {
            no_results_text.text = getString(R.string.empty_search_result, it)
        }

        observe(searchViewModel.loadMoreStatus) {
            if (it?.isRunning == true) {
                load_more_bar.visibility = View.VISIBLE
            } else {
                load_more_bar.visibility = View.GONE
                it?.errorMessageIfNotHandled?.let { error ->
                    Snackbar.make(load_more_bar, error, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun initSearchInputListener() {
        input.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        input.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }
    }

    private fun doSearch(v: View) {
        val query = input.text.toString()
        // Dismiss keyboard
        dismissKeyboard(v.windowToken)
        searchViewModel.setQuery(query)
    }

    private fun initRecyclerView() {
        repo_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1) {
                    searchViewModel.loadNextPage()
                }
            }
        })
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    /**
     * Created to be able to override in tests
     */
    fun navController() = findNavController()
}
