package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun SuccessDialog(
    visible: Boolean,
    isLoan: Boolean,
    offeredAmount: String,
    beneficiaryName: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    if (visible) {
        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = Shapes().medium,
                modifier = modifier.size(320.dp, 340.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 19.dp)
                ) {
                    IconButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = ""
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Image(
                        painter = painterResource(SharedRes.images.ic_thank_you_icon),
                        contentDescription = ""
                    )

                    if (isLoan) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(
                                SharedRes.strings.loan_offer_made_message,
                                offeredAmount,
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    } else {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(
                                SharedRes.strings.donation_made_message,
                                offeredAmount,
                                beneficiaryName
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}