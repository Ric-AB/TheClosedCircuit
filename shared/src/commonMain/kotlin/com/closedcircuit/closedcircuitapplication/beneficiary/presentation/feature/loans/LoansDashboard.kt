package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.components.WalletCard
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.preview.LoansPreviewScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


internal class LoansDashboard : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ScreenContent(
            goBack = navigator::pop,
            navigateToLoansPreview = { navigator.push(LoansPreviewScreen(it)) }
        )
    }

    @Composable
    private fun ScreenContent(goBack: () -> Unit, navigateToLoansPreview: (LoanStatus) -> Unit) {
        BaseScaffold(
            topBar = {
                DefaultAppBar(
                    title = stringResource(SharedRes.strings.loans_label),
                    mainAction = goBack
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
            ) {
                val commonModifier = remember { Modifier.fillMaxWidth() }

                WalletCard(null, commonModifier)

                Spacer(Modifier.height(80.dp))
                Sections(modifier = commonModifier, navigateToLoansPreview = navigateToLoansPreview)
            }
        }
    }

    @Composable
    private fun Sections(modifier: Modifier, navigateToLoansPreview: (LoanStatus) -> Unit) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LoanSection(
                modifier = modifier,
                imageResource = SharedRes.images.ic_calendar,
                text = stringResource(SharedRes.strings.repayment_schedule_label),
                onClick = {}
            )

            LoanSection(
                modifier = modifier,
                imageResource = SharedRes.images.ic_timer,
                text = stringResource(SharedRes.strings.pending_loan_offers_label),
                onClick = { navigateToLoansPreview(LoanStatus.PENDING) }
            )

            LoanSection(
                modifier = modifier,
                imageResource = SharedRes.images.ic_bullseye,
                text = stringResource(SharedRes.strings.accepted_loan_offers_label),
                onClick = { navigateToLoansPreview(LoanStatus.ACCEPTED) }
            )

            LoanSection(
                modifier = modifier,
                imageResource = SharedRes.images.ic_hand,
                text = stringResource(SharedRes.strings.active_loans_label),
                onClick = { navigateToLoansPreview(LoanStatus.PAID) }
            )

            LoanSection(
                modifier = modifier,
                imageResource = SharedRes.images.ic_declined,
                text = stringResource(SharedRes.strings.loans_declined_label),
                onClick = { navigateToLoansPreview(LoanStatus.DECLINED) }
            )
        }
    }

    @Composable
    private fun LoanSection(
        modifier: Modifier,
        imageResource: ImageResource,
        text: String,
        onClick: () -> Unit,
    ) {
        ElevatedCard(
            modifier = modifier.clickable { onClick() },
            colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(imageResource),
                    backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(Elevation.Level1),
                    tint = MaterialTheme.colorScheme.primary,
                    shape = Shapes().medium,
                    size = 24.dp,
                    padding = 12.dp
                )

                Spacer(Modifier.width(16.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
            }
        }
    }

    @Composable
    private fun Icon(
        modifier: Modifier = Modifier,
        painter: Painter,
        backgroundColor: Color,
        tint: Color,
        shape: Shape,
        size: Dp,
        padding: Dp,
        contentDescription: String? = null
    ) {
        Box(
            modifier = modifier.background(backgroundColor, shape).padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                tint = tint,
                modifier = Modifier.size(size)
            )
        }
    }
}