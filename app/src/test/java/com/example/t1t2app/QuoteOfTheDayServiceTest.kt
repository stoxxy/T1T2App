package com.example.t1t2app

import com.example.t1t2app.quoteoftheday.data.Quote
import com.example.t1t2app.quoteoftheday.data.QuoteOfTheDayService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.create

class QuoteOfTheDayServiceTest {
    @get: Rule
    val mockWebServer = MockWebServer()
    private lateinit var quoteOfTheDayService: QuoteOfTheDayService

    @Before
    fun setUp() {
        mockWebServer.start()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(Json {}.asConverterFactory("application/json".toMediaType()))
            .build()
        quoteOfTheDayService = retrofit.create<QuoteOfTheDayService>()
    }

    @Test
    fun getQuotes_success() = runTest {
        val fakeQuotes = listOf(
            Quote(quote = "Test quote 1", author = "Author 1"),
            Quote(quote = "Test quote 2", author = "Author 2")
        )
        mockWebServer.enqueue(
            MockResponse().setBody(Json.encodeToString(ListSerializer(Quote.serializer()),fakeQuotes))
        )

        val response = quoteOfTheDayService.getQuotes()
        assertEquals(fakeQuotes, response)
    }
}