package com.example.t1t2app

import com.example.t1t2app.quoteoftheday.data.Quote
import com.example.t1t2app.quoteoftheday.data.QuoteOfTheDayImpl
import com.example.t1t2app.quoteoftheday.data.QuoteOfTheDayService
import com.example.t1t2app.quoteoftheday.domain.QuoteOfTheDayApi
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuoteOfTheDayImplTest {
    @get: Rule
    val mockkRule = MockKRule(this)
    @MockK
    private lateinit var quoteOfTheDayService: QuoteOfTheDayService

    private lateinit var quoteApi: QuoteOfTheDayApi

    @Before
    fun setUp() {
        quoteApi = QuoteOfTheDayImpl(quoteOfTheDayService)
    }

    @Test
    fun returnsListOfQuotes() = runTest {
        val fakeQuotes = listOf(
            Quote(quote = "Test quote 1", author = "Author 1"),
            Quote(quote = "Test quote 2", author = "Author 2")
        )

        coEvery { quoteOfTheDayService.getQuotes() } returns fakeQuotes

        with(quoteApi.getQuotes()) {
            assertEquals(fakeQuotes, this)
            assertEquals(2, this.size)
        }
    }

}