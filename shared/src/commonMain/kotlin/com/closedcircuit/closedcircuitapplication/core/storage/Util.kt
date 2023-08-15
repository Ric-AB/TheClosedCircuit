package com.closedcircuit.closedcircuitapplication.core.storage

var storageDir = ""

object Storage {
    val appSettingsFile = "${storageDir}/app_settings.json"
    val sessionFile = "${storageDir}/session.json"
}