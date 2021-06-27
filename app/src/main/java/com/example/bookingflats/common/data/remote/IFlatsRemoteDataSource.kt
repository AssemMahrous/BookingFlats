package com.example.bookingflats.common.data.remote

import com.example.bookingflats.basemodule.base.data.remote.IRemoteDataSource
import com.example.bookingflats.common.data.remote.api.FlatsApi

interface IFlatsRemoteDataSource : IRemoteDataSource {
    val flatsApi: FlatsApi
}