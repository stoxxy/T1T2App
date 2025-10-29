package com.example.t1t2app.quoteoftheday.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.t1t2app.quoteoftheday.data.Quote

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteOfTheDayScreen(
    viewModel: QuoteOfTheDayViewModel,
    modifier: Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    PullToRefreshBox(
        isRefreshing = uiState.result is QuoteOfTheDayViewModel.Result.InProgress,
        onRefresh = {
            viewModel.fetchQuotes()
        },
        modifier = modifier.fillMaxSize()
    ) {
        QuoteOfTheDayContent(
            uiState = uiState,
            modifier = Modifier
        )
    }
}

@Composable
fun QuoteOfTheDayContent(
    uiState: QuoteOfTheDayViewModel.UiState,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        AnimatedContent(
            targetState = uiState.result,
            label = "QuoteContentAnimation",
            transitionSpec = {
                (slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight / 4 },
                    animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
                )).togetherWith(
                    slideOutVertically(
                        targetOffsetY = { fullHeight -> -fullHeight / 4 },
                        animationSpec = tween(durationMillis = 400, easing = FastOutLinearInEasing)
                    ) + fadeOut(
                        animationSpec = tween(durationMillis = 400, easing = FastOutLinearInEasing)
                    )
                ).using(
                    SizeTransform(
                        clip = true,
                        sizeAnimationSpec = { _, _ ->
                            tween(durationMillis = 400, easing = FastOutSlowInEasing)
                        }
                    )
                )
            },
            modifier = modifier.fillMaxSize()
        ) { state ->
            when (state) {
                is QuoteOfTheDayViewModel.Result.InProgress -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is QuoteOfTheDayViewModel.Result.Failure -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = state.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                is QuoteOfTheDayViewModel.Result.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.quotes) {
                            QuoteComponent(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuoteComponent(quote: Quote) {
    Card {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(quote.quote)
        }
    }
}