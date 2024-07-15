package com.closedcircuit.closedcircuitapplication.common.domain.util

import dev.gitlive.firebase.storage.Data
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import platform.Foundation.NSData
import platform.Foundation.create

@OptIn(ExperimentalForeignApi::class)
actual fun getFirebaseDataObj(bytes: ByteArray): Data {
    memScoped {
        return Data(
            NSData.create(
                bytes = allocArrayOf(bytes),
                length = bytes.size.toULong()
            )
        )
    }
}