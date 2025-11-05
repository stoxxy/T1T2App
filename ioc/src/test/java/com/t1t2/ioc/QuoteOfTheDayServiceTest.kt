package com.example.t1t2app

import com.example.t1t2app.quoteoftheday.data.Quote
import com.example.t1t2app.quoteoftheday.data.QuoteOfTheDayService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit

class QuoteOfTheDayServiceTest {
    @get: Rule
    val mockWebServer = MockWebServer()
    private lateinit var quoteOfTheDayService: QuoteOfTheDayService
    private lateinit var testLogger: TestLogger

    class TestLogger: HttpLoggingInterceptor.Logger {
        private val logs = mutableListOf<String>()
        override fun log(message: String) {
            logs.add(message)
        }
        fun logsEmpty() = logs.isEmpty()
    }

    @Before
    fun setUp() {
        testLogger = TestLogger()

        val loggingInterceptor = HttpLoggingInterceptor(testLogger).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(Json {}.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
        quoteOfTheDayService = retrofit.create<QuoteOfTheDayService>()
    }

    @Test
    fun getQuotes_success_logsRequestAndResponse() = runTest {
        val fakeQuotes = listOf(
            Quote(quote = "Test quote 1", author = "Author 1"),
            Quote(quote = "Test quote 2", author = "Author 2")
        )
        mockWebServer.enqueue(
            MockResponse().setBody(Json.encodeToString(ListSerializer(Quote.serializer()),fakeQuotes))
        )

        val response = quoteOfTheDayService.getQuotes()
        assertEquals(fakeQuotes, response)

        assertFalse(testLogger.logsEmpty())
    }
}