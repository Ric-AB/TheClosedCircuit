package com.closedcircuit.closedcircuitapplication.core.network

interface Response<T> {
    val isSuccessful: Boolean
    val code: Int
    val description: String

    fun body(): T?

    suspend fun bodyAsString(): String
    fun headers(): Set<Map.Entry<String, List<String>>>
}