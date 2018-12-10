package com.android.example.github.presentation.util

import android.content.Context
import androidx.annotation.StringRes

class StringOrResourceId {

    private var string: String? = null
    private var stringRes: Int? = null

    constructor(string: String) {
        this.string = string
    }
    constructor(@StringRes stringRes: Int) {
        this.stringRes = stringRes
    }

    fun getString(context: Context): String {
        string?.let {
            return it
        } ?: return context.getString(stringRes!!)
    }

    // Looses lint error check here for mismatching arguments for a string format
    // Might need some fancy lint checking to get it back
    fun getString(context: Context, vararg arguments: String): String {
        return context.getString(stringRes!!, arguments)
    }
}