package com.android.example.github.repository

sealed class DomainException : Throwable() {
    class NotAuthorised : DomainException()
    class MalformedResponse : DomainException()
}
