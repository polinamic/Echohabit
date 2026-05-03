package com.echohabit.app.core.di

import com.echohabit.app.data.repository.BehaviorRepositoryImpl
import com.echohabit.app.domain.repository.BehaviorRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Singleton
    @Binds
    abstract fun bindBehaviorRepository(
        impl: BehaviorRepositoryImpl
    ): BehaviorRepository
}
