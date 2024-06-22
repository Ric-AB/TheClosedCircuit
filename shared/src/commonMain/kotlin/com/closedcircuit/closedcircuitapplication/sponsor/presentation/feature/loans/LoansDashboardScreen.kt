package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans.loanlist.LoansScreen
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

class LoansDashboardScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ScreenContent(
            goBack = navigator::pop,
            navigateToLoansScreen = { navigator.push(LoansScreen(it)) }
        )
    }

    @Composable
    private fun ScreenContent(goBack: () -> Unit, navigateToLoansScreen: (LoanStatus) -> Unit) {
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
                Spacer(Modifier.height(80.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LoanSection(
                        modifier = Modifier.fillMaxWidth(),
                        imagePainter = painterResource(SharedRes.images.ic_hand),
                        text = stringResource(SharedRes.strings.loan_offers_label),
                        onClick = { navigateToLoansScreen(LoanStatus.PENDING) }
                    )

                    LoanSection(
                        modifier = Modifier.fillMaxWidth(),
                        imagePainter = rememberVectorPainter(Icons.Filled.Check),
                        text = stringResource(SharedRes.strings.accepted_loan_offers_label),
                        onClick = { navigateToLoansScreen(LoanStatus.ACCEPTED) }
                    )

                    LoanSection(
                        modifier = Modifier.fillMaxWidth(),
                        imagePainter = painterResource(SharedRes.images.ic_recurring_icon),
                        text = stringResource(SharedRes.strings.current_loans_label),
                        onClick = { navigateToLoansScreen(LoanStatus.PAID) }
                    )

                    LoanSection(
                        modifier = Modifier.fillMaxWidth(),
                        imagePainter = painterResource(SharedRes.images.ic_bulb_cancelled),
                        text = stringResource(SharedRes.strings.loans_declined_label),
                        onClick = { navigateToLoansScreen(LoanStatus.DECLINED) }
                    )
                }
            }
        }
    }

    @Composable
    private fun LoanSection(
        modifier: Modifier,
        imagePainter: Painter,
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
                    painter = imagePainter,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.width(16.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }
    }
}