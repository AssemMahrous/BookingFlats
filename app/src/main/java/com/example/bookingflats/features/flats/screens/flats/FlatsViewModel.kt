package com.example.bookingflats.features.flats.screens.flats

import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.bookingflats.basemodule.base.common.IApplicationContext
import com.example.bookingflats.basemodule.base.data.local.IDeviceLocationManager
import com.example.bookingflats.basemodule.base.platform.BaseViewModel
import com.example.bookingflats.basemodule.utils.Constants.LATITUDE
import com.example.bookingflats.basemodule.utils.Constants.LONGITUDE
import com.example.bookingflats.basemodule.utils.getKoinInstance
import com.example.bookingflats.common.data.FlatsRemoteMediator
import com.example.bookingflats.common.data.local.db.BookingFlatsDatabase
import com.example.bookingflats.features.flats.module.Mapper.toFlatView
import com.example.bookingflats.features.flats.module.usecase.GetFlatsUseCase
import com.example.bookingflats.features.flats.module.view.FlatView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FlatsViewModel(
    private val getFlatsUseCase: GetFlatsUseCase,
    private val deviceLocationManager: IDeviceLocationManager
) : BaseViewModel() {
    private val applicationContext by getKoinInstance<IApplicationContext>()

    fun searchFlats(
        numberOfBeds: Int?,
        endDate: Long?,
        StartDate: Long?,
        isGranted: Boolean
    ): Flow<PagingData<FlatView>> {
        var userLat: Double = LATITUDE
        var userLng: Double = LONGITUDE
        if (isGranted)
            deviceLocationManager.getCurrentLocation {
                userLat = it?.latitude ?: LATITUDE
                userLng = it?.longitude ?: LONGITUDE
            }
        val pagingSourceFactory = {
            BookingFlatsDatabase.getInstance(applicationContext.context)
                .flatsDao().flatsByQuery(
                    numberOfBeds = numberOfBeds,
                    endDate = endDate,
                    startDate = StartDate
                )
        }
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            pagingSourceFactory = pagingSourceFactory,
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            remoteMediator = FlatsRemoteMediator(
                bookingFlatsDatabase = BookingFlatsDatabase.getInstance(applicationContext.context),
                userLat = userLat,
                userLng = userLng
            ) {
                getFlatsUseCase(
                    userLat, userLng
                )
            }
        ).flow
            .map { pagingData ->
                pagingData.map {
                    it.toFlatView()
                }
            }.cachedIn(viewModelScope)

    }
}