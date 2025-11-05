package com.example.t1t2app.quoteoftheday.data

import retrofit2.http.GET

interface QuoteOfTheDayService {
    @GET("api/quotes")
    suspend fun getQuotes(): List<Quote>
}