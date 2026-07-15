package ir.ayantech.networking

import android.app.Application
import ir.ayantech.networking.di.dataModule
import ir.ayantech.networking.di.domainModule
import ir.ayantech.networking.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin

class NetworkApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            AndroidLogger()
            androidContext(this@NetworkApplication)
            modules(
                dataModule,
                domainModule,
                uiModule
            )
        }
    }
}