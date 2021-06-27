package com.example.bookingflats.common.data.local

import com.example.bookingflats.basemodule.base.data.local.IDataBase
import com.example.bookingflats.basemodule.base.data.local.SharedPreferencesInterface


class ApplicationLocalDataSource(
    override val sharedPreferences: SharedPreferencesInterface,
    override val dataBase: IDataBase
) : IApplicationLocalDataSource