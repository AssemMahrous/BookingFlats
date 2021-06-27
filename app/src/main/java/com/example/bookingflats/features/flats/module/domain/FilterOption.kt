package com.example.bookingflats.features.flats.module.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class FilterOption(
    val startDate: Long?,
    val endDate: Long?,
    val numberOfBedrooms: Int?
) : Parcelable, Serializable