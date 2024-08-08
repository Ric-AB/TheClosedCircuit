package com.closedcircuit.closedcircuitapplication.common.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.primary6
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource

internal class SuccessScreen(
    private val title: String,
    private val message: String = "",
    private val primaryAction: () -> Unit
) : Screen {
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
                .padding(horizontal = horizontalScreenPadding)
                .padding(bottom = verticalScreenPadding)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(top = 100.dp)
                    .size(150.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = primary6,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))
            BodyText(
                text = subTitle,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.weight(1f))
            DefaultButton(onClick = primaryAction) {
                Text(text = stringResource(SharedRes.strings.proceed))
            }
        }
    }
}