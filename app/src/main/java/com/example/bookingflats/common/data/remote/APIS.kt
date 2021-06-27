package com.example.bookingflats.common.data.remote

import com.example.bookingflats.BuildConfig.BASE_URL

object APIS {
    private const val PATH_JSON = "apartments.json"
    const val APARTMENTS_URL = "$BASE_URL$PATH_JSON"
}