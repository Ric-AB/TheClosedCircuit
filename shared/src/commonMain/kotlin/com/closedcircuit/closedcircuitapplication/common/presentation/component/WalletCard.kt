package com.closedcircuit.closedcircuitapplication.common.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.wallet.Wallet

@Composable
fun WalletCard(amount: String?, modifier: Modifier) {
    @Composable
    fun Card(modifier: Modifier, containerColor: Color) {
        androidx.compose.material3.Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = containerColor)
        ) {}
    }

    Box(
        modifier = modifier.fillMaxWidth()
            .height(180.dp),
        contentAlignment = Alignment.Center
    ) {
        val commonModifier = Modifier.padding(horizontal = 8.dp)
            .matchParentSize()

        Card(
            modifier = commonModifier.rotate(5F),
            containerColor = Color.LightGray
        )

        Card(
            modifier = commonModifier.rotate(355F),
            containerColor = Color.LightGray
        )

        Card(
            modifier = Modifier.matchParentSize(),
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )

        Text(
            text = amount ?: "--",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}