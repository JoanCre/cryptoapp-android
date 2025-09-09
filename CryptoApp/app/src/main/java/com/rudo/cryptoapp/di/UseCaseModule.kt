package com.rudo.cryptoapp.di

import com.rudo.cryptoapp.domain.repository.CryptoRepository
import com.rudo.cryptoapp.domain.usecase.CryptoUseCase
import com.rudo.cryptoapp.domain.usecase.CryptoUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideCryptoUseCase(repository: CryptoRepository): CryptoUseCase =
        CryptoUseCaseImpl(repository)
}
