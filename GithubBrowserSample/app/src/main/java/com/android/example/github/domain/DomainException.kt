package com.android.example.github.domain

sealed class DomainException : Throwable() {
    class ItsTooLateAtNight : DomainException()
    class NotAuthorised : DomainException()
    class MalformedResponse : DomainException()
    class NoNetwork : DomainException()
}
