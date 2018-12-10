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

package com.android.example.github.data.github

import com.android.example.github.testing.OpenForTesting
import org.threeten.bp.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Here is for deciding where to get data, from network/memory/disk
 */
@Singleton
@OpenForTesting
class TimeOfDayRepository @Inject constructor() {

    // We can cache things in memory in repositories if we like. Eg:
    var serversNeedToSleep: Boolean = true

    fun isSleepingTime(): Boolean {
        return serversNeedToSleep && LocalTime.now().isAfter(LocalTime.of(22, 0))
    }
}