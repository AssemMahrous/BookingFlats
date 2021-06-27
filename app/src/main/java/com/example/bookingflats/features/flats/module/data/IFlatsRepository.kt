package com.example.bookingflats.features.flats.module.data

import com.example.bookingflats.basemodule.Result
import com.example.bookingflats.basemodule.base.platform.IBaseRepository
import com.example.bookingflats.features.flats.module.domain.Flat

interface IFlatsRepository : IBaseRepository {
    suspend fun getFlats(userLat: Double, userLng: Double): Result<List<Flat>>
    fun bookFlat(startDate: Long, endDate: Long, id: String)
}