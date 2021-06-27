package com.example.bookingflats.features.flats.module.usecase

import com.example.bookingflats.basemodule.base.platform.BaseUseCase
import com.example.bookingflats.features.flats.module.data.IFlatsRepository

class BookFlatUseCase(repository: IFlatsRepository) : BaseUseCase<IFlatsRepository>(repository) {
    fun invoke(startDate: Long, endDate: Long, id: String) {
        return repository.bookFlat(startDate, endDate, id)
    }
}