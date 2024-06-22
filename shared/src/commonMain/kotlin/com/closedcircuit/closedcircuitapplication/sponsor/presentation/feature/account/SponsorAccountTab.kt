package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.WalletCard
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.findRootNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans.LoansDashboardScreen
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

object SponsorAccountTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(SharedRes.strings.account)
            val icon = rememberVectorPainter(Icons.Outlined.AccountBox)

            return remember {
                TabOptions(
                    index = 3u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = findRootNavigator(LocalNavigator.currentOrThrow)
        ScreenContent(
            navigateToLoansDashboard = { navigator.push(LoansDashboardScreen()) }
        )
    }

    @Composable
    private fun ScreenContent(navigateToLoansDashboard: () -> Unit) {
        BaseScaffold { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
                    .statusBarsPadding()
                    .padding(horizontal = horizontalScreenPadding)
            ) {
                WalletCard(wallet = null, modifier = Modifier.fillMaxWidth())

                Spacer(Modifier.height(100.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Section(
                        modifier = Modifier.fillMaxWidth(),
                        iconRes = SharedRes.images.ic_hand_with_money,
                        textRes = SharedRes.strings.loans_label,
                        onClick = navigateToLoansDashboard
                    )

                    Section(
                        modifier = Modifier.fillMaxWidth(),
                        iconRes = SharedRes.images.ic_credit_card,
                        textRes = SharedRes.strings.card_details_label,
                        onClick = { }
                    )

                    Section(
                        modifier = Modifier.fillMaxWidth(),
                        iconRes = SharedRes.images.ic_bank_colored,
                        textRes = SharedRes.strings.bank_details_label,
                        onClick = { }
                    )

                    Section(
                        modifier = Modifier.fillMaxWidth(),
                        iconRes = SharedRes.images.ic_hand_with_money,
                        textRes = SharedRes.strings.withdraw_fund_label,
                        onClick = { }
                    )
                }
            }
        }
    }

    @Composable
    private fun Section(
        modifier: Modifier,
        iconRes: ImageResource,
        textRes: StringResource,
        onClick: () -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.clickable(onClick = onClick)
                .padding(vertical = 20.dp)
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(Elevation.Level1),
                        shape = CircleShape
                    )
                    .padding(10.dp)
            )

            Spacer(Modifier.width(16.dp))
            Text(
                text = stringResource(textRes),
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }

}