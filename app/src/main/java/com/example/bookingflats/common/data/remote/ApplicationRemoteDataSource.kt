package com.example.bookingflats.common.data.remote

import com.example.bookingflats.basemodule.base.data.remote.RemoteDataSourceImpl
import retrofit2.Retrofit


class ApplicationRemoteDataSource(override val retrofit: Retrofit)
    : RemoteDataSourceImpl(retrofit), IApplicationRemoteDataSource