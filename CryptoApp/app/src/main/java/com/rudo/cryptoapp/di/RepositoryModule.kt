package com.rudo.cryptoapp.di

import com.rudo.cryptoapp.data.datasource.CryptoDataSource
import com.rudo.cryptoapp.data.repository.CryptoRepositoryImpl
import com.rudo.cryptoapp.domain.repository.CryptoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCryptoRepository(
        remoteDataSource: CryptoDataSource
    ): CryptoRepository = CryptoRepositoryImpl(remoteDataSource)
}
