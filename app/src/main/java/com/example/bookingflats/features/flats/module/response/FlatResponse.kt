package com.example.bookingflats.features.flats.module.response

import com.google.gson.annotations.SerializedName

data class FlatResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("bedrooms") val bedrooms: Int
)
