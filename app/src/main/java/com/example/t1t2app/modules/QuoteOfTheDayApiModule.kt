package com.example.t1t2app.modules

import com.example.t1t2app.quoteoftheday.data.QuoteOfTheDayImpl
import com.example.t1t2app.quoteoftheday.data.QuoteOfTheDayService
import com.example.t1t2app.quoteoftheday.domain.QuoteOfTheDayApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class QuoteOfTheDayApiModule {
    @Singleton
    @Binds
    abstract fun bindQuoteOfTheDayImpl(
        quoteOfTheDayImpl: QuoteOfTheDayImpl
    ): QuoteOfTheDayApi

    companion object {
        private val JSON = Json { ignoreUnknownKeys = true }
        @Provides
        fun provideQuotesRetrofit(): Retrofit = Retrofit.Builder()
            .addConverterFactory(JSON
                .asConverterFactory("application/json".toMediaType()))
            .baseUrl("https://zenquotes.io")
            .build()

        @Provides
        fun provideQuoteOfTheDayService(
            retrofit: Retrofit
        ) = retrofit.create<QuoteOfTheDayService>()
    }
}