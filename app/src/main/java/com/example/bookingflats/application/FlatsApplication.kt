package com.example.bookingflats.application

import com.example.bookingflats.BuildConfig
import com.example.bookingflats.basemodule.base.BaseApplication
import com.example.bookingflats.di.FeaturesKoinModules
import okhttp3.Authenticator
import org.koin.core.module.Module
import timber.log.Timber
import java.util.*

class FlatsApplication : BaseApplication() {
    override val modulesList: ArrayList<Module>
        get() = FeaturesKoinModules.allModules
    override val authenticator: Authenticator
        get() = Authenticator.NONE
    override val baseUrl: String
        get() = BuildConfig.BASE_URL
    override val sharedPreferencesName: String
        get() = "flats"
    override val isDebug: Boolean
        get() = BuildConfig.DEBUG

    override fun onCreate() {
        FeaturesKoinModules.init()
        super.onCreate()
    }

    override fun getTimberReleaseTree(): Timber.Tree {
        return object : Timber.Tree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            }
        }
    }
}