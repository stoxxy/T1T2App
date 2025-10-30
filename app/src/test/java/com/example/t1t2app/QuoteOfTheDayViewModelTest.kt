package com.example.t1t2app

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.t1t2app.quoteoftheday.data.Quote
import com.example.t1t2app.quoteoftheday.domain.QuoteOfTheDayApi
import com.example.t1t2app.quoteoftheday.domain.QuoteOfTheDayRepository
import com.example.t1t2app.quoteoftheday.presentation.QuoteOfTheDayViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.coEvery
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class, sdk = [34])
class QuoteOfTheDayViewModelTest {

    @get: Rule(order = 0)
    val dispatcherRule = TestDispatcherRule()
    @get: Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var quoteOfTheDayRepository: QuoteOfTheDayRepository
    lateinit var viewModel: QuoteOfTheDayViewModel

    @Before
    fun setUp() {
        hiltRule.inject()

        viewModel = QuoteOfTheDayViewModel(
            quoteOfTheDayRepository = quoteOfTheDayRepository,
            dispatcher = dispatcherRule.dispatcher
        )
    }

    @Test
    fun getQuote_success_uiStateUpdatedWithQuotes() = runTest {
        val fakeQuotes = listOf(
            Quote(quote = "Test quote 1", author = "Author 1"),
            Quote(quote = "Test quote 2", author = "Author 2")
        )
        coEvery { quoteOfTheDayRepository.getQuotes() } returns fakeQuotes

        viewModel.uiState.test {
            assertEquals(QuoteOfTheDayViewModel.Result.InProgress, awaitItem().result)
            val successResult = awaitItem().result
            assertTrue(successResult is QuoteOfTheDayViewModel.Result.Success)
            assertEquals(fakeQuotes, (successResult as QuoteOfTheDayViewModel.Result.Success).quotes)
        }
    }

    @Test
    fun getQuote_error_uiStateUpdatedWithError() = runTest {
        val exception = Exception("Test exception")
        coEvery { quoteOfTheDayRepository.getQuotes() } throws exception

        viewModel.uiState.test {
            assertEquals(QuoteOfTheDayViewModel.Result.InProgress, awaitItem().result)
            val successResult = awaitItem().result
            assertTrue(successResult is QuoteOfTheDayViewModel.Result.Failure)
            assertEquals(exception.message, (successResult as QuoteOfTheDayViewModel.Result.Failure).error)
        }
    }
}