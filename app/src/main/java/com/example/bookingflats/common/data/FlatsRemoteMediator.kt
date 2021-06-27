/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bookingflats.common.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.bookingflats.basemodule.Result
import com.example.bookingflats.basemodule.base.data.local.BookingFlatsDatabase
import com.example.bookingflats.basemodule.base.data.model.FlatDbEntity
import com.example.bookingflats.features.flats.module.Mapper.toFlatDbEntity
import com.example.bookingflats.features.flats.module.domain.Flat
import retrofit2.HttpException
import java.io.IOException

// GitHub page API is 1 based: https://developer.github.com/v3/#pagination
private const val GITHUB_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class FlatsRemoteMediator(
    private val bookingFlatsDatabase: BookingFlatsDatabase,
    private val userLat: Double,
    private val userLng: Double,
    val call: suspend () -> Result<List<Flat>>,
) : RemoteMediator<Int, FlatDbEntity>() {

    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FlatDbEntity>
    ): MediatorResult {

        try {
            val apiResponse = call()
            (return when (apiResponse) {
                is Result.Success -> {
                    val data = apiResponse.data
                    bookingFlatsDatabase.withTransaction {
                        for (item in data) {
                            val fetchedItem = bookingFlatsDatabase.flatsDao().getItemById(item.id)
                            if (fetchedItem.isEmpty())
                                bookingFlatsDatabase.flatsDao()
                                    .insert(item.toFlatDbEntity(userLat, userLng))
                        }
                    }
                    MediatorResult.Success(endOfPaginationReached = true)
                }
                is Result.Error -> MediatorResult.Error(apiResponse.exception)
            })

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

}
