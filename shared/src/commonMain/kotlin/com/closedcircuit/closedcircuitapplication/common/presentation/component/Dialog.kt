package com.closedcircuit.closedcircuitapplication.common.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun PromptDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    title: String,
    message: String,
    buttonText: String,
    onDismiss: () -> Unit,
    onButtonClick: () -> Unit
) {
    if (visible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Column(
                modifier = modifier.background(color = Color.White, shape = Shapes().medium)
                    .padding(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text(text = message, style = MaterialTheme.typography.labelMedium)

                Spacer(Modifier.height(24.dp))
                DefaultButton(
                    modifier = Modifier.fillMaxWidth(0.5F).align(Alignment.CenterHorizontally),
                    onClick = {
                        onDismiss()
                        onButtonClick()
                    }
                ) {
                    Text(text = buttonText)
                }
            }
        }
    }
}

@Composable
fun AppAlertDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    confirmTitle: String,
    properties: DialogProperties = DialogProperties()
) {
    if (visible) {
        Dialog(onDismissRequest = onDismissRequest, properties = properties) {
            Card(
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                modifier = modifier
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    TitleText(title)

                    Spacer(Modifier.height(16.dp))
                    BodyText(text)

                    Spacer(Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismissRequest) {
                            Text(stringResource(SharedRes.strings.cancel_label))
                        }

                        TextButton(onClick = { onDismissRequest(); onConfirm() }) {
                            Text(confirmTitle)
                        }
                    }
                }
            }
        }
    }
}