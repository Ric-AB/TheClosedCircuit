package com.closedcircuit.closedcircuitapplication.presentation.feature.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.WalletCard
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
        ScreenContent()
    }

    @Composable
    private fun ScreenContent() {
        BaseScaffold { innerPadding ->

            Column(
                modifier = Modifier.padding(innerPadding)
                    .statusBarsPadding()
                    .padding(horizontal = horizontalScreenPadding)
            ) {
                WalletCard(wallet = null, modifier = Modifier.fillMaxWidth())

                Spacer(Modifier.height(40.dp))
                AccountSections(Modifier.fillMaxWidth())
            }
        }
    }

    @Composable
    private fun AccountSections(modifier: Modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = modifier.background(color = Color.White, shape = Shapes().medium)
                .padding(vertical = 20.dp)
        ) {
            val commonModifier = remember { Modifier.fillMaxWidth() }
            Section(
                modifier = commonModifier,
                iconRes = SharedRes.images.ic_loans,
                textRes = SharedRes.strings.loans_label,
                onClick = {}
            )

            Section(
                modifier = commonModifier,
                iconRes = SharedRes.images.ic_credit_card,
                textRes = SharedRes.strings.card_details_label,
                onClick = {}
            )

            Section(
                modifier = commonModifier,
                iconRes = SharedRes.images.ic_bank,
                textRes = SharedRes.strings.bank_details_label,
                onClick = {}
            )

            Section(
                modifier = commonModifier,
                iconRes = SharedRes.images.ic_national_id,
                textRes = SharedRes.strings.kyc_label,
                onClick = {}
            )
        }
    }

    @Composable
    private fun Section(
        modifier: Modifier,
        iconRes: ImageResource,
        textRes: StringResource,
        onClick: () -> Unit
    ) {
        Row(modifier = modifier.clickable(onClick = onClick).padding(horizontal = 16.dp)) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clip(CircleShape)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(Elevation.Level1),
                        shape = CircleShape
                    )
            )

            Spacer(Modifier.width(16.dp))
            BodyText(
                text = stringResource(textRes),
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
        }
    }
}