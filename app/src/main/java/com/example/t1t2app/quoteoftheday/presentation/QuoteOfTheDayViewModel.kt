package com.example.t1t2app.quoteoftheday.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.t1t2app.quoteoftheday.data.Quote
import com.example.t1t2app.quoteoftheday.domain.QuoteOfTheDayApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuoteOfTheDayViewModel @Inject constructor(
    private val quoteOfTheDayApi: QuoteOfTheDayApi,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.
            onStart {
                fetchQuotes()
            }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    fun fetchQuotes() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(result = Result.InProgress) }
                // Prevent PullToRefresh indicator from getting stuck.
                delay(10)
                _uiState.update { it.copy(
                    result = Result.Success(withContext(dispatcher) { quoteOfTheDayApi.getQuotes() })) }
            } catch (e: Exception) {
                _uiState.update { it.copy(result = Result.Failure(e.message ?: e.toString())) }
            }
        }
    }

    data class UiState(
        val result: Result = Result.InProgress
    )

    sealed class Result {
        data object InProgress: Result()
        data class Success(val quotes: List<Quote>): Result()
        data class Failure(val error: String): Result()
    }
}