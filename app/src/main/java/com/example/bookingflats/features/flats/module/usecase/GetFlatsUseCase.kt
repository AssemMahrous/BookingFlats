package com.example.bookingflats.features.flats.module.usecase

import com.example.bookingflats.basemodule.Result
import com.example.bookingflats.basemodule.base.platform.BaseUseCase
import com.example.bookingflats.features.flats.module.data.IFlatsRepository
import com.example.bookingflats.features.flats.module.domain.Flat

class GetFlatsUseCase(repository: IFlatsRepository) : BaseUseCase<IFlatsRepository>(repository) {
    suspend operator fun invoke(userLat: Double, userLng: Double): Result<List<Flat>> {
        return repository.getFlats(userLat, userLng)
    }
}