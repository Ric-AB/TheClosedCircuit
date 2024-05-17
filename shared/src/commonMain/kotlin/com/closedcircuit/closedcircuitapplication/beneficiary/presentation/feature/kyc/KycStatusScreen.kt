package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.kyc

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.KycDocumentType
import com.closedcircuit.closedcircuitapplication.common.domain.model.KycStatus
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.findRootNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.secondary3
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


@OptIn(ExperimentalMaterial3Api::class)
internal class KycStatusScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.getNavigatorScreenModel<KycViewModel>()

        viewModel.state?.let {
            ScreenContent(
                state = it,
                goBack = { if (navigator.canPop) navigator.pop() else findRootNavigator(navigator).pop() },
                onEvent = viewModel::onEvent,
                navigateToSelectDocumentTypeScreen = { navigator.push(SelectDocumentTypeScreen()) },
                navigateToDocumentNumberScreen = { navigator.push(EnterDocumentNumberScreen()) }
            )
        }
    }

    @Composable
    private fun ScreenContent(
        state: KycUiState,
        goBack: () -> Unit,
        onEvent: (KycUiEvent) -> Unit,
        navigateToSelectDocumentTypeScreen: () -> Unit,
        navigateToDocumentNumberScreen: () -> Unit
    ) {
        BaseScaffold(
            topBar = {
                DefaultAppBar(
                    title = stringResource(SharedRes.strings.account_status),
                    mainAction = goBack
                )
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .padding(
                        horizontal = horizontalScreenPadding,
                        vertical = verticalScreenPadding
                    )
            ) {
                val kycStatus = state.kycStatus
                val phoneStatus = state.phoneStatus
                var showPendingSnackbar by remember {
                    mutableStateOf(kycStatus == KycStatus.PENDING)
                }

                val idVerificationRes = remember { getResources(state.kycStatus) }
                if (idVerificationRes != null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    VerificationStatus(
                        iconResource = SharedRes.images.ic_national_id,
                        textResource = SharedRes.strings.identity_document_verification_label,
                        subTextResource = idVerificationRes.first,
                        trailingIconResource = idVerificationRes.second,
                        onClick = if (kycStatus == KycStatus.FAILED) {
                            { navigateToSelectDocumentTypeScreen() }
                        } else null
                    )
                }

                val phoneVerificationResources = remember { getResources(phoneStatus) }
                if (phoneVerificationResources != null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    VerificationStatus(
                        iconResource = SharedRes.images.ic_phone,
                        textResource = SharedRes.strings.phone_number_verification,
                        subTextResource = phoneVerificationResources.first,
                        trailingIconResource = phoneVerificationResources.second,
                        onClick = if (phoneStatus == KycStatus.FAILED || phoneStatus == KycStatus.NOT_STARTED) {
                            {
                                onEvent(KycUiEvent.DocumentTypeChange(KycDocumentType.PHONE_NUMBER))
                                navigateToDocumentNumberScreen()
                            }
                        } else null
                    )
                }

                if (showPendingSnackbar) {
                    VerificationPendingSnackBar { showPendingSnackbar = false }
                }
            }
        }
    }

    @Composable
    private fun VerificationStatus(
        iconResource: ImageResource,
        textResource: StringResource,
        subTextResource: StringResource,
        trailingIconResource: ImageResource?,
        onClick: (() -> Unit)? = null
    ) {
        Card(
            onClick = {
                if (onClick != null) {
                    onClick()
                }
            },
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(iconResource),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(textResource),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    BodyText(text = stringResource(subTextResource))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (trailingIconResource != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource(trailingIconResource),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp),
                        )
                    }

                    if (onClick != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun VerificationPendingSnackBar(dismissSnackbar: () -> Unit) {
        Card(
            onClick = { },
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = secondary3)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(SharedRes.images.ic_pending),
                    contentDescription = "",
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(SharedRes.strings.verification_pending_label),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(SharedRes.strings.verification_inprogess_prompt),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                IconButton(onClick = { dismissSnackbar() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                }
            }
        }
    }

    private fun getResources(status: KycStatus): Pair<StringResource, ImageResource>? {
        return when (status) {
            KycStatus.PENDING -> Pair(
                SharedRes.strings.verification_pending_label,
                SharedRes.images.ic_pending
            )

            KycStatus.VERIFIED -> Pair(
                SharedRes.strings.verified_label,
                SharedRes.images.ic_green_check,
            )

            KycStatus.FAILED -> Pair(
                SharedRes.strings.verification_failed_label,
                SharedRes.images.ic_failed
            )

            KycStatus.NOT_STARTED -> null
        }
    }
}
