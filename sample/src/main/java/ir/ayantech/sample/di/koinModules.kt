package ir.ayantech.sample.di

import ir.ayantech.networking.ayanModel.Language
import ir.ayantech.networking.ayanModel.LogLevel
import ir.ayantech.networking.v2.AyanApi
import ir.ayantech.sample.data.APIs
import ir.ayantech.sample.data.repository.DonationRepositoryImpl
import ir.ayantech.sample.data.source.DonationRemoteDataSource
import ir.ayantech.sample.data.source.DonationRemoteDataSourceImpl
import ir.ayantech.sample.domain.repository.DonationRepository
import ir.ayantech.sample.domain.usecase.GetDonationUseCase
import ir.ayantech.sample.domain.usecase.GetDonationUseCaseImpl
import ir.ayantech.sample.ui.MainViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.seconds

val dataModule = module {
    single<AyanApi> {
        AyanApi.Builder(context = androidContext(), APIs.BASE_URL)
            .setLogLevel(LogLevel.LOG_ALL)
            .setTimeOutDuration(20.seconds)
            .setAcceptLanguage(Language.PERSIAN)
            .setFollowRedirect(true)
            .setInvokeUserToken {
                return@setInvokeUserToken "user_token"
            }
            .build()
    }

    single<DonationRemoteDataSource> { DonationRemoteDataSourceImpl(ayanApi = get()) }
    single<DonationRepository> { DonationRepositoryImpl(remoteSource = get()) }
}

val domainModule = module {
    single<CoroutineContext> {
        Dispatchers.Default
    }
    factory<GetDonationUseCase> {
        GetDonationUseCaseImpl(
            repository = get(),
            coroutineContext = get()
        )
    }
}

val uiModule = module {
    viewModel {
        MainViewModel(donationUseCase = get())
    }
}