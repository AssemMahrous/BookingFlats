package com.example.bookingflats.basemodule.base.platform

abstract class BaseUseCase<Repository : IBaseRepository>(val repository: Repository)