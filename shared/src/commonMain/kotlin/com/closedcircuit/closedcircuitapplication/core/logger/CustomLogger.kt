package com.closedcircuit.closedcircuitapplication.core.logger

import io.github.aakira.napier.Napier
import io.ktor.client.plugins.logging.Logger

class CustomLogger : Logger {
    override fun log(message: String) {
        Napier.d(message)
    }
}