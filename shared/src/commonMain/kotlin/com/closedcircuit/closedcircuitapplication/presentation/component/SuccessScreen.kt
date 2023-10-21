package com.closedcircuit.closedcircuitapplication.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.SlideUpTransition
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding

internal data class SuccessScreen(
    val title: String,
    val message: String = "",
    val primaryAction: () -> Unit
) : Screen, CustomScreenTransition by SlideUpTransition {
    @Composable
    override fun Content() {
        ScreenContent(title, message, primaryAction)
    }
}

@Composable
private fun ScreenContent(title: String, subTitle: String, primaryAction: () -> Unit) {
    BaseScaffold { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = defaultHorizontalScreenPadding)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(top = 100.dp)
                    .size(150.dp)
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(text = title, textAlign = TextAlign.Center)
            Text(
                text = subTitle,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.weight(1f))
            DefaultButton(onClick = primaryAction) {
                Text(text = "Proceed")
            }
        }
    }
}