package com.example.bookingflats.features.flats.module.data

import com.example.bookingflats.basemodule.Result
import com.example.bookingflats.basemodule.base.platform.BaseRepository
import com.example.bookingflats.common.data.local.IFlatsLocalDataSource
import com.example.bookingflats.common.data.remote.IFlatsRemoteDataSource
import com.example.bookingflats.features.flats.module.Mapper.toFLats
import com.example.bookingflats.features.flats.module.domain.Flat
import com.example.bookingflats.features.flats.module.response.FlatResponse

class FlatsRepository(
    flatsLocalDataSource: IFlatsLocalDataSource, flatsRemoteDataSource: IFlatsRemoteDataSource
) : BaseRepository<IFlatsLocalDataSource, IFlatsRemoteDataSource>(
    flatsLocalDataSource, flatsRemoteDataSource
), IFlatsRepository {
    override suspend fun getFlats(userLat: Double, userLng: Double): Result<List<Flat>> {
        return safeApiCallWithHeaders(networkCall = {
            remoteDataSource.flatsApi.getFlats()
        }, successHandler = { list: List<FlatResponse>, _: HashMap<String, String> ->
            list.toFLats(userLat, userLng)
        }
        )
    }
}