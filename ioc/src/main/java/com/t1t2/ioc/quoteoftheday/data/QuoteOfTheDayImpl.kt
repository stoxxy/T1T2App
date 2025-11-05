package com.example.t1t2app.quoteoftheday.data

import com.example.t1t2app.quoteoftheday.domain.QuoteOfTheDayApi
import javax.inject.Inject

class QuoteOfTheDayImpl @Inject constructor(
    private val service: QuoteOfTheDayService
): QuoteOfTheDayApi {
    override suspend fun getQuotes(): List<Quote> {
        return service.getQuotes()
    }
}