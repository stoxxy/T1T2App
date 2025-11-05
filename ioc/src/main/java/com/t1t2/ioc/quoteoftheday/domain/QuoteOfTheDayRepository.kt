package com.example.t1t2app.quoteoftheday.domain

import com.example.t1t2app.quoteoftheday.data.Quote

interface QuoteOfTheDayRepository {
    suspend fun getQuotes(): List<Quote>
}