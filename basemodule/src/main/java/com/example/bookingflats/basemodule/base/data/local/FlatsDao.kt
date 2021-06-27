package com.example.bookingflats.basemodule.base.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookingflats.basemodule.base.data.model.FlatDbEntity

@Dao
interface FlatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<FlatDbEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(flatDbEntity: FlatDbEntity)

    @Query(
        """SELECT * FROM flats 
        WHERE (:numberOfBeds IS NULL OR bedrooms LIKE :numberOfBeds) 
        AND ( :bookedDate  > bookedDate) 
         ORDER BY distance ASC"""
    )
    fun flatsByQuery(
        numberOfBeds: Int? = null,
        bookedDate: Long? = null
    ): PagingSource<Int, FlatDbEntity>

    @Query("UPDATE flats SET bookedDate = :bookedDate WHERE id= :id")
    fun updateFlat(
        bookedDate: Long,
        id: String
    )

    @Query("SELECT * from flats WHERE id= :id")
    fun getItemById(id: String): List<FlatDbEntity>

    @Query("DELETE FROM flats")
    suspend fun clearFlats()
}
