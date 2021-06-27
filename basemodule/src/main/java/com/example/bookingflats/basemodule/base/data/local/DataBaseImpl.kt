package com.example.bookingflats.basemodule.base.data.local

import android.content.Context
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DataBaseImpl constructor(
    context: Context
) : IDataBase {

    private val database: BookingFlatsDatabase
    private val flatsDao: FlatsDao
    private val ioExecutor: Executor

    init {
        database = BookingFlatsDatabase.getInstance(context)
        flatsDao = database.flatsDao()
        ioExecutor = Executors.newSingleThreadExecutor()
    }

    override fun updateFlat(id: String, bookedDate: Long) {
        ioExecutor.execute {
            flatsDao.updateFlat(
                bookedDate = bookedDate,
                id = id
            )
        }
    }
}