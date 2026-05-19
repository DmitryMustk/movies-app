package com.example.moviesapp.di

import com.example.moviesapp.BuildConfig
import com.example.moviesapp.data.remote.omdb.OmdbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOmdbAuthInterceptor(): Interceptor =
        Interceptor { chain ->
            val original = chain.request()
            val originalUrl = original.url

            val urlWithKey = originalUrl.newBuilder()
                .addQueryParameter("apikey", BuildConfig.OMDB_API_KEY)
                .build()

            val request = original.newBuilder()
                .url(urlWithKey)
                .build()

            chain.proceed(request)
        }

    @Provides
    @Singleton
    fun provideOmdbOkHttpClient(
        logging: HttpLoggingInterceptor,
        omdbAuthInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(omdbAuthInterceptor)
            .addInterceptor(logging)
            .build()

    @Provides
    @Singleton
    @Named("omdbRetrofit")
    fun provideOmdbRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.OMDB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideOmdbApi(
        @Named("omdbRetrofit") retrofit: Retrofit
    ): OmdbApi =
        retrofit.create(OmdbApi::class.java)

}
