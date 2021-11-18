package com.deitel.winfoxtesttask1.di

import com.deitel.winfoxtesttask1.api.BackendAPI
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    fun provideUnsplashService(): BackendAPI {
        return BackendAPI.create()
    }
}