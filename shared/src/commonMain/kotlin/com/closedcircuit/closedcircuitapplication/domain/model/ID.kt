package com.closedcircuit.closedcircuitapplication.domain.model

inline class ID(val value: String) {
    init {
        require(value.isNotEmpty())
    }
}