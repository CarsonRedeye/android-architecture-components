package com.android.example.github.ui.repo

import com.android.example.github.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.example.github.vo.Repo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.repo_item.*

class RepoItemViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    companion object {
        fun create(parent: ViewGroup) = RepoItemViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.repo_item,
                                 parent,
                                 false))
    }

    fun bind(repo: Repo, repoClickCallback: ((Repo) -> Unit)?) {
        name.text = repo.fullName
        desc.text = repo.description
        stars.text = repo.stars.toString()
        containerView.setOnClickListener {
            repoClickCallback?.invoke(repo)
        }
    }
}