package com.example.bookingflats.common.data.remote

import com.example.bookingflats.basemodule.base.data.remote.RemoteDataSourceImpl
import com.example.bookingflats.common.data.remote.api.FlatsApi
import retrofit2.Retrofit

class ApplicationRemoteDataSource(override val retrofit: Retrofit) : RemoteDataSourceImpl(retrofit),
    IApplicationRemoteDataSource {
    override val flatsApi: FlatsApi = retrofit.create(FlatsApi::class.java)
}