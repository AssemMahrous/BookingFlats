package com.example.bookingflats.di


import com.example.bookingflats.common.data.local.ApplicationLocalDataSource
import com.example.bookingflats.common.data.local.IApplicationLocalDataSource
import com.example.bookingflats.common.data.remote.ApplicationRemoteDataSource
import com.example.bookingflats.common.data.remote.IApplicationRemoteDataSource
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.*

object FeaturesKoinModules {

    private lateinit var appNetworkModule: Module
    private lateinit var appCacheModule: Module
    private lateinit var appAnalyticsModule: Module
    private lateinit var appDestinationsModule: Module

    val allModules: ArrayList<Module>
        get() {
            val list = arrayListOf<Module>()

            // general
            list.add(appCacheModule)
            list.add(appNetworkModule)
            list.add(appAnalyticsModule)
            list.add(appDestinationsModule)

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
                    get()
                )
            }
        }
    }

    private fun initAppNetworkModule() {
        appNetworkModule = module {
            single<IApplicationRemoteDataSource> { ApplicationRemoteDataSource(get()) }
        }
    }
}
