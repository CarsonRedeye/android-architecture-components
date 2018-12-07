package com.android.example.github.ui.util

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T : Any, L : LiveData<T>> Fragment.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(this.viewLifecycleOwner, Observer(body))
}

fun TextView.setTextVisibleOrNullGone(text: String?)  {
    if (text != null) {
        this.text = text
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}
