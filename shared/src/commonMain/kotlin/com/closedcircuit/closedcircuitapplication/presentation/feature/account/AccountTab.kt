package com.closedcircuit.closedcircuitapplication.presentation.feature.account

import androidx.compose.material3.ElevatedCard
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
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.WalletCard
import com.closedcircuit.closedcircuitapplication.presentation.feature.kyc.KycNavigator
import com.closedcircuit.closedcircuitapplication.presentation.feature.loans.LoansDashboard
import com.closedcircuit.closedcircuitapplication.presentation.navigation.findRootNavigator
import com.closedcircuit.closedcircuitapplication.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

internal object AccountTab : Tab {

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
            navigateToKycScreen = { navigator.push(KycNavigator()) },
            navigateToLoansDashboard = { navigator.push(LoansDashboard()) }
        )
    }

    @Composable
    private fun ScreenContent(
        navigateToKycScreen: () -> Unit,
        navigateToLoansDashboard: () -> Unit
    ) {
        BaseScaffold { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
                    .statusBarsPadding()
                    .padding(horizontal = horizontalScreenPadding)
            ) {
                WalletCard(wallet = null, modifier = Modifier.fillMaxWidth())

                Spacer(Modifier.height(100.dp))
                AccountSections(
                    modifier = Modifier.fillMaxWidth(),
                    navigateToKycScreen = navigateToKycScreen,
                    navigateToLoansDashboard = navigateToLoansDashboard
                )
            }
        }
    }

    @Composable
    private fun AccountSections(
        modifier: Modifier,
        navigateToKycScreen: () -> Unit,
        navigateToLoansDashboard: () -> Unit
    ) {
        ElevatedCard(
            colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        ) {
            Column(modifier = modifier.background(color = Color.White, shape = Shapes().medium)) {
                val commonModifier = remember { Modifier.fillMaxWidth() }
                Section(
                    modifier = commonModifier,
                    iconRes = SharedRes.images.ic_loans,
                    textRes = SharedRes.strings.loans_label,
                    showDivider = true,
                    onClick = navigateToLoansDashboard
                )

                Section(
                    modifier = commonModifier,
                    iconRes = SharedRes.images.ic_credit_card,
                    textRes = SharedRes.strings.card_details_label,
                    showDivider = true,
                    onClick = {}
                )

                Section(
                    modifier = commonModifier,
                    iconRes = SharedRes.images.ic_bank,
                    textRes = SharedRes.strings.bank_details_label,
                    showDivider = true,
                    onClick = {}
                )

                Section(
                    modifier = commonModifier,
                    iconRes = SharedRes.images.ic_national_id,
                    textRes = SharedRes.strings.kyc_label,
                    showDivider = false,
                    onClick = navigateToKycScreen
                )
            }
        }
    }

    @Composable
    private fun Section(
        modifier: Modifier,
        iconRes: ImageResource,
        textRes: StringResource,
        showDivider: Boolean,
        onClick: () -> Unit
    ) {
        Column(modifier = modifier.clickable(onClick = onClick).padding(horizontal = 16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 20.dp)
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
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

                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
            }

            if (showDivider) {
                Divider(color = Color.LightGray.copy(alpha = 0.5f))
            }
        }
    }
}