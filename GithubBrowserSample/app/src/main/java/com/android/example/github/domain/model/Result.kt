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

package com.android.example.github.domain.model

import com.android.example.github.domain.model.Status.ERROR
import com.android.example.github.domain.model.Status.LOADING
import com.android.example.github.domain.model.Status.SUCCESS
import com.android.example.github.presentation.util.StringOrResourceId

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */

/** This will be replaced by RxJava */
data class Result<out T>(val status: Status, val data: T?, val message: StringOrResourceId?) {
    companion object {
        fun <T> success(data: T?): Result<T> {
            return Result(SUCCESS, data, null)
        }

        fun <T> error(msg: StringOrResourceId, data: T? = null): Result<T> {
            return Result(ERROR, data, msg)
        }

        fun <T> loading(data: T? = null): Result<T> {
            return Result(LOADING, data, null)
        }
    }
}

