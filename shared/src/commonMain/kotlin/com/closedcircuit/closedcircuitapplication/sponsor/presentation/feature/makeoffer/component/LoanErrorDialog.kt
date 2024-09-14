package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun LoanErrorDialog(
    visible: Boolean,
    prompt: String,
    onDismiss: () -> Unit,
    onClick: (() -> Unit)?
) {
    if (visible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape =MaterialTheme.shapes.medium,
                modifier = Modifier.width(350.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 12.dp)
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = ""
                        )
                    }

                    Image(
                        painter = painterResource(SharedRes.images.ic_red_caution),
                        contentDescription = ""
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    if (onClick != null) {
                        Spacer(modifier = Modifier.height(40.dp))
                        DefaultButton(onClick = { onDismiss(); onClick() }) {
                            Text(stringResource(SharedRes.strings.donate_x_label))
                        }
                    }
                }
            }
        }
    }
}