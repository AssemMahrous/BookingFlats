package com.example.bookingflats.basemodule.base.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookingflats.basemodule.base.data.model.FlatDbEntity

@Database(
    entities = [FlatDbEntity::class],
    version = 2,
    exportSchema = false
)
abstract class BookingFlatsDatabase : RoomDatabase() {

    abstract fun flatsDao(): FlatsDao

    companion object {

        @Volatile
        private var INSTANCE: BookingFlatsDatabase? = null

        fun getInstance(context: Context): BookingFlatsDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                BookingFlatsDatabase::class.java, "bookingFlats.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}
