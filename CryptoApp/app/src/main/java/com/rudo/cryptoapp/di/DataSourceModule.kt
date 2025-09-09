package com.rudo.cryptoapp.di

import com.rudo.cryptoapp.data.datasource.CryptoDataSource
import com.rudo.cryptoapp.data.datasource.remote.CryptoRemoteDataSourceImpl
import com.rudo.cryptoapp.data.datasource.remote.api.CryptoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    
    @Provides
    @Singleton
    fun provideCryptoDataSource(cryptoApi: CryptoApi): CryptoDataSource =
        CryptoRemoteDataSourceImpl(cryptoApi)
}
