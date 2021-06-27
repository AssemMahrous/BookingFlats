package com.example.bookingflats.basemodule.base.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "flats")
data class FlatDbEntity(
    @PrimaryKey @field:SerializedName("id") val id: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("latitude") val latitude: Double,
    @field:SerializedName("longitude") val longitude: Double,
    @field:SerializedName("bedrooms") val bedrooms: Int,
    @field:SerializedName("BookedDate") val bookedDate: Long? = null,
    @field:SerializedName("distance") val distance: Double,
)
