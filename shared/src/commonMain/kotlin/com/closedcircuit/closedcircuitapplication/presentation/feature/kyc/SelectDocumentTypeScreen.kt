@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.presentation.feature.kyc

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.domain.model.AccountType
import com.closedcircuit.closedcircuitapplication.domain.model.KycDocumentType
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


internal class SelectDocumentTypeScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<KycViewModel>()
        ScreenContent(
            state = screenModel.state!!,
            goBack = navigator::pop,
            onEvent = screenModel::onEvent,
            navigateToDocumentNumberScreen = { navigator.push(EnterDocumentNumberScreen()) }
        )
    }
}

@Composable
private fun ScreenContent(
    state: KycUiState,
    goBack: () -> Unit,
    onEvent: (KycUiEvent) -> Unit,
    navigateToDocumentNumberScreen: () -> Unit
) {
    BaseScaffold(
        topBar = {
            DefaultAppBar(
                title = stringResource(SharedRes.strings.document_verification),
                mainAction = goBack
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = horizontalScreenPadding,
                    vertical = verticalScreenPadding
                )
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Image(
                painter = painterResource(SharedRes.images.kyc_illurstration),
                contentDescription = null,
                modifier = Modifier.size(240.dp, 190.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(SharedRes.strings.select_document_to_verify),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = stringResource(SharedRes.strings.we_need_to_determine_document_is_authentic),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
            )

            if (state.accountType == AccountType.NG) {
                Spacer(modifier = Modifier.height(40.dp))
                SelectableDocumentType(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(SharedRes.strings.bvn_display_text),
                    iconResource = SharedRes.images.ic_id_card,
                    onClick = {
                        onEvent(KycUiEvent.DocumentTypeChange(KycDocumentType.BVN))
                        navigateToDocumentNumberScreen()
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            SelectableDocumentType(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(SharedRes.strings.nin_display_text),
                iconResource = SharedRes.images.ic_national_id,
                onClick = {
                    val docType = if (state.accountType == AccountType.NG) KycDocumentType.NIN_SLIP
                    else KycDocumentType.NATIONAL_ID_NO_PHOTO
                    onEvent(KycUiEvent.DocumentTypeChange(docType))
                    navigateToDocumentNumberScreen()
                }
            )
        }
    }
}

@Composable
private fun SelectableDocumentType(
    modifier: Modifier = Modifier,
    text: String,
    iconResource: ImageResource,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier,
        onClick = onClick,
        shape = Shapes().small,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Icon(
                painter = painterResource(iconResource),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
            )

            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            Icon(imageVector = Icons.Rounded.KeyboardArrowRight, contentDescription = null)
        }
    }
}