package com.example.bookingflats.common.data.remote.api

import com.example.bookingflats.common.data.remote.APIS.APARTMENTS_URL
import com.example.bookingflats.features.flats.module.response.FlatResponse
import retrofit2.Response
import retrofit2.http.GET

interface FlatsApi {
    @GET(APARTMENTS_URL)
    suspend fun getFlats()
            : Response<List<FlatResponse>>
}