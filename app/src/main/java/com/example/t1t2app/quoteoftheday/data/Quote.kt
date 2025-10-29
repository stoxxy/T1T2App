package com.example.t1t2app.quoteoftheday.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    @SerialName("q")
    val quote: String,

    @SerialName("a")
    val author: String,
)
