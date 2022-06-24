package com.example.movie.di

import com.example.movie.data.Storage
import com.example.movie.model.User
import com.example.movie.model.repository.*
import com.example.movie.service.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieApiService(@RetrofitModule.Movie retrofit: Retrofit): MovieApiService =
        retrofit.create(MovieApiService::class.java)

    @Provides
    @Singleton
    fun provideSerialsApiService(@RetrofitModule.Movie retrofit: Retrofit): SerialsApiService =
        retrofit.create(SerialsApiService::class.java)

    @Provides
    @Singleton
    fun providePeopleApiService(@RetrofitModule.Movie retrofit: Retrofit): PeopleApiService =
        retrofit.create(PeopleApiService::class.java)

    @Provides
    @Singleton
    fun provideUserApiService(@RetrofitModule.Login retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun provideSearchApiService(@RetrofitModule.Movie retrofit: Retrofit): SearchApiService =
        retrofit.create(SearchApiService::class.java)

    @Provides
    @Singleton
    fun provideSearchRepository(searchApiService: SearchApiService): SearchRepository =
        SearchRepository(searchApiService)

    @Provides
    @Singleton
    fun provideSerialRepository(serialsApiService: SerialsApiService): SerialsRepository =
        SerialsRepository(serialsApiService)

    @Provides
    @Singleton
    fun providePeopleRepository(peopleApiService: PeopleApiService): PeopleRepository =
        PeopleRepository(peopleApiService)

    @Provides
    @Singleton
    fun provideUserRepository(
        userApiService: UserApiService,
        storage: Storage,
        user: User
    ): UserRepository = UserRepository(userApiService, storage, user)

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieApiService: MovieApiService,
        user: User
    ): MovieRepository = MovieRepository(movieApiService, user)
}
