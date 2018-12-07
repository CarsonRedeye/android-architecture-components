package com.android.example.github.data.util

import com.android.example.github.domain.DomainException
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

fun <T> Single<T>.threadForNetwork(): Single<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

/**
 * This lets us decide what to do with network errors and make our own exception types
 *
 * In the view we can then just do:
 * if (throwable is DomainException.NotAuthorised) {
 *     errorMessage.text = "Sorry you're not allowed in here"
 * }
 * */
fun <T> Single<T>.mapErrors(): Single<T> {
    return this.onErrorResumeNext {
        when (it) {
            is SocketTimeoutException -> Single.error(TimeoutException())
            is HttpException -> Single.error(DomainException.NotAuthorised())
            else -> Single.error(it)
        }
    }
}