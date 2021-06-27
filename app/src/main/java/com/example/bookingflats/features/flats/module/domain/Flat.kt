package com.example.bookingflats.features.flats.module.domain

data class Flat(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val bedrooms: Int,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val distance: Double,
)
