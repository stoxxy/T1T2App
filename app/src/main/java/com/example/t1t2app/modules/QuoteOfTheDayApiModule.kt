package com.example.t1t2app.modules

import com.example.t1t2app.BuildConfig
import com.example.t1t2app.quoteoftheday.data.QuoteOfTheDayImpl
import com.example.t1t2app.quoteoftheday.data.QuoteOfTheDayRepositoryImpl
import com.example.t1t2app.quoteoftheday.data.QuoteOfTheDayService
import com.example.t1t2app.quoteoftheday.domain.QuoteOfTheDayApi
import com.example.t1t2app.quoteoftheday.domain.QuoteOfTheDayRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class QuoteOfTheDayApiModule {
    @Singleton
    @Binds
    abstract fun bindQuoteOfTheDayRepository(
        quoteOfTheDayRepositoryImpl: QuoteOfTheDayRepositoryImpl
    ): QuoteOfTheDayRepository

    @Singleton
    @Binds
    abstract fun bindQuoteOfTheDayApi(
        quoteOfTheDayImpl: QuoteOfTheDayImpl
    ): QuoteOfTheDayApi

    companion object {
        @Provides
        fun provideJson() = Json { ignoreUnknownKeys = true }
        @Provides
        fun provideBaseUrl() = "https://zenquotes.io"

        @Provides
        fun provideLoggingInterceptor() =
             HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
                }

        @Provides
        fun provideOkHttpClient(
            interceptor: HttpLoggingInterceptor
        ) = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        @Provides
        fun provideQuotesRetrofit(
            json: Json,
            baseUrl: String,
            client: OkHttpClient
        ): Retrofit = Retrofit.Builder()
            .addConverterFactory(json
                .asConverterFactory("application/json".toMediaType()))
            .baseUrl(baseUrl)
            .client(client)
            .build()

        @Provides
        fun provideQuoteOfTheDayService(
            retrofit: Retrofit
        ) = retrofit.create<QuoteOfTheDayService>()
    }
}