package com.example.t1t2app.quoteoftheday.data

import com.example.t1t2app.quoteoftheday.domain.QuoteOfTheDayApi
import com.example.t1t2app.quoteoftheday.domain.QuoteOfTheDayRepository
import javax.inject.Inject

class QuoteOfTheDayRepositoryImpl @Inject constructor(
    private val quoteOfTheDayApi: QuoteOfTheDayApi
): QuoteOfTheDayRepository {
    override suspend fun getQuotes() = quoteOfTheDayApi.getQuotes()
}