package com.closedcircuit.closedcircuitapplication.common.presentation.util

import android.content.Context
import android.content.Intent

class AndroidShareHandler(private val context: Context) : ShareHandler {
    override fun sharePlanLink(text: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, "Sponsor my project")
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        try {
            context.startActivity(Intent.createChooser(shareIntent, "Choose one"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}