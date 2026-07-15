package ir.ayantech.networking.di

import ir.ayantech.ayannetworking.ayanModel.Language
import ir.ayantech.ayannetworking.ayanModel.LogLevel
import ir.ayantech.ayannetworking.v2.AyanApi
import ir.ayantech.networking.data.APIs
import ir.ayantech.networking.data.repository.DonationRepositoryImpl
import ir.ayantech.networking.data.source.DonationRemoteDataSource
import ir.ayantech.networking.data.source.DonationRemoteDataSourceImpl
import ir.ayantech.networking.domain.repository.DonationRepository
import ir.ayantech.networking.domain.usecase.GetDonationUseCase
import ir.ayantech.networking.domain.usecase.GetDonationUseCaseImpl
import ir.ayantech.networking.ui.MainViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
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