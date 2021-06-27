package com.example.bookingflats.basemodule.base.data.local

interface LocalDataSource {
    val sharedPreferences: SharedPreferencesInterface
    val dataBase: IDataBase
}