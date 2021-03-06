package com.example.bookingflats.basemodule

import com.example.bookingflats.basemodule.utils.ApplicationException
import okhttp3.Headers

sealed class Result<out R> {
    data class Success<T>(val data: T, var headers: Headers? = null) : Result<T>()
    data class Error(val exception: ApplicationException) : Result<Nothing>()
}
