package com.closedcircuit.closedcircuitapplication.common.presentation.feature.message

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource

internal object MessageTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(SharedRes.strings.message)
            val icon = rememberVectorPainter(Icons.Outlined.Email)

            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {

    }
}