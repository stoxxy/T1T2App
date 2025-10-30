package com.example.t1t2app

import com.example.t1t2app.modules.QuoteOfTheDayApiModule
import com.example.t1t2app.quoteoftheday.domain.QuoteOfTheDayRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [QuoteOfTheDayApiModule::class]
)
object FakeQuoteOfTheDayApiModule {
    @Singleton
    @Provides
    fun provideMockedQuoteOfTheDayRepository() = mockk<QuoteOfTheDayRepository>(relaxed = true)
}