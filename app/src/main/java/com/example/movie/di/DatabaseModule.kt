package com.example.movie.di

import android.content.Context
import com.example.movie.data.Storage
import com.example.movie.model.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideContext(@ApplicationContext appContext: Context): Context = appContext

    @Provides
    @Singleton
    fun provideStorage(context: Context) = Storage(context)

    @Provides
    @Singleton
    fun provideUser() = User(null, null, null, null, null)
}
