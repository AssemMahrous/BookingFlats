package com.example.bookingflats.features.flats.module

import com.example.bookingflats.basemodule.utils.getDistance
import com.example.bookingflats.features.flats.module.domain.Flat
import com.example.bookingflats.features.flats.module.domain.FlatDbEntity
import com.example.bookingflats.features.flats.module.response.FlatResponse
import com.example.bookingflats.features.flats.module.view.FlatView

object Mapper {

    fun List<FlatResponse>.toFLats(userLat: Double, userLng: Double) =
        map {
            it.toFlat(userLat, userLng)
        }

    fun FlatResponse.toFlat(userLat: Double, userLng: Double) =
        Flat(
            id = id,
            name = name,
            latitude = latitude,
            longitude = longitude,
            bedrooms = bedrooms,
            distance = getDistance(userLng, userLat, longitude, latitude)
        )

    fun List<Flat>.toFLatsDbEntity(userLat: Double, userLng: Double) =
        map {
            it.toFlatDbEntity(userLat, userLng)
        }

    fun Flat.toFlatDbEntity(userLat: Double, userLng: Double) =
        FlatDbEntity(
            id = id,
            name = name,
            latitude = latitude,
            longitude = longitude,
            bedrooms = bedrooms,
            distance = getDistance(userLng, userLat, longitude, latitude)
        )

    fun FlatDbEntity.toFlatView() =
        FlatView(
            id = id,
            name = name,
            distance = distance,
            bedrooms = bedrooms
        )
}