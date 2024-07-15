package com.closedcircuit.closedcircuitapplication.common.domain.util

import dev.gitlive.firebase.storage.Data

actual fun getFirebaseDataObj(bytes: ByteArray): Data {
    return Data(bytes)
}