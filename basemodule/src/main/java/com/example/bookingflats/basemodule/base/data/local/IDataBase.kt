package com.example.bookingflats.basemodule.base.data.local

interface IDataBase {
    fun updateFlat(id: String, startDate: Long, endDate: Long)
}