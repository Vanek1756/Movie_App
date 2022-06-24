package com.example.movie.di

import android.content.Context
import com.example.movie.BuildConfig
import com.example.movie.R
import com.example.movie.util.API_KEY_V3
import com.example.movie.util.AuthInterceptor
import com.example.movie.util.MovieInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseUrl

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Login

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Movie

    @BaseUrl
    @Provides
    fun provideBaseUrl(@ApplicationContext appContext: Context): String =
        appContext.getString(R.string.retrofit_base_url)

    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    fun provideAuthInterceptor() = AuthInterceptor(API_KEY_V3)

    @Provides
    fun provideMovieInterceptor() = MovieInterceptor(API_KEY_V3)

    @Login
    @Provides
    fun provideOkHttp(
        authInterceptor: AuthInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Login
    @Provides
    @Singleton
    fun provideRetrofit(
        @BaseUrl BASE_URL: String,
        @Login okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Movie
    @Provides
    fun provideMovieOkHttp(
        movieInterceptor: MovieInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(movieInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Movie
    @Provides
    @Singleton
    fun provideMovieRetrofit(
        @BaseUrl BASE_URL: String,
        @Movie okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
