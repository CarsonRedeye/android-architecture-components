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

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.android.example.github.R
import com.android.example.github.application.di.Injectable
import com.android.example.github.domain.model.Status
import com.android.example.github.presentation.search.adapter.RepoListAdapter
import com.android.example.github.presentation.util.observe
import com.android.example.github.testing.OpenForTesting
import kotlinx.android.synthetic.main.loading_state.*
import kotlinx.android.synthetic.main.search_fragment.*
import javax.inject.Inject

@OpenForTesting
class SearchFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var adapter: RepoListAdapter? = null

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
        val rvAdapter = RepoListAdapter { repo ->
            // This should probably be handled by viewModel, for easier testing?
            /*navController().navigate(
                    //SearchFragmentDirections.showRepo(repo.owner.login, repo.name)
            )*/
        }
        repo_list.adapter = rvAdapter
        adapter = rvAdapter

        initSearchInputListener()

        retry.setOnClickListener { searchViewModel.refresh() }
        observeViewModel(searchViewModel)
    }

    fun observeViewModel(searchViewModel: SearchViewModel) {

        observe(searchViewModel.results) {
            // Possible style to shorten the visible/gone switching using android-ktx extensions
            // (I think I prefer this to help not to forget to hide)
            progress_bar.isVisible = (it?.status == Status.LOADING)

            adapter?.submitList(it?.data)
            no_results_text.isVisible = (it?.data?.isNullOrEmpty() == true)


            if (it?.status == Status.ERROR) {
                retry.visibility = View.VISIBLE
                error_msg.text = it.message?.getString(context!!) ?: getString(R.string.unknown_error)
            } else {
                retry.visibility = View.GONE
                error_msg.visibility = View.GONE
            }
        }

        observe(searchViewModel.query) {
            no_results_text.text = getString(R.string.empty_search_result, it)
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

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    /**
     * Created to be able to override in tests
     */
    fun navController() = findNavController()
}
