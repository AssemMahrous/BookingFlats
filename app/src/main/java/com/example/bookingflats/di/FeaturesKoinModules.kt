package com.example.bookingflats.di


import com.example.bookingflats.common.data.local.ApplicationLocalDataSource
import com.example.bookingflats.common.data.local.IApplicationLocalDataSource
import com.example.bookingflats.common.data.local.IFlatsLocalDataSource
import com.example.bookingflats.common.data.remote.ApplicationRemoteDataSource
import com.example.bookingflats.common.data.remote.IApplicationRemoteDataSource
import com.example.bookingflats.common.data.remote.IFlatsRemoteDataSource
import com.example.bookingflats.features.flats.module.data.FlatsRepository
import com.example.bookingflats.features.flats.module.data.IFlatsRepository
import com.example.bookingflats.features.flats.module.usecase.BookFlatUseCase
import com.example.bookingflats.features.flats.module.usecase.GetFlatsUseCase
import com.example.bookingflats.features.flats.screens.flats.FlatsViewModel
import com.example.bookingflats.features.flats.screens.main.MainViewModel
import com.example.bookingflats.features.flats.screens.preview.PreviewViewModel
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.experimental.builder.factory
import java.util.*

object FeaturesKoinModules {

    private lateinit var appNetworkModule: Module
    private lateinit var appCacheModule: Module

    val allModules: ArrayList<Module>
        get() {
            val list = arrayListOf<Module>()

            // general
            list.add(appCacheModule)
            list.add(appNetworkModule)

            // flats
            list.add(module {
                //view models
                viewModel<MainViewModel>()
                viewModel<PreviewViewModel>()
                viewModel<FlatsViewModel>()

                //use cases
                factory<GetFlatsUseCase>()
                factory<BookFlatUseCase>()

                //repositories
                factory<IFlatsRepository> { FlatsRepository(get(), get()) }
            })

            return list
        }

    fun init() {
        // remote data source
        initAppNetworkModule()

        // local data source
        initAppCacheModule()

    }

    private fun initAppCacheModule() {
        appCacheModule = module {
            single<IApplicationLocalDataSource> {
                ApplicationLocalDataSource(
                    get(),
                    get()
                )
            }
            single<IFlatsLocalDataSource> { get<IApplicationLocalDataSource>() }
        }
    }

    private fun initAppNetworkModule() {
        appNetworkModule = module {
            single<IApplicationRemoteDataSource> { ApplicationRemoteDataSource(get()) }
            single<IFlatsRemoteDataSource> { get<IApplicationRemoteDataSource>() }
        }
    }
}
