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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultVerticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


internal class SelectVerificationTypeScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ScreenContent(goBack = navigator::pop)
    }
}

@Composable
private fun ScreenContent(goBack: () -> Unit) {
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
                .padding(horizontal = defaultHorizontalScreenPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = defaultVerticalScreenPadding)
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Image(
                painter = painterResource(SharedRes.images.kyc_illurstration),
                contentDescription = null,
                modifier = Modifier.size(240.dp, 190.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(SharedRes.strings.select_document_to_verify),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(SharedRes.strings.we_need_to_determine_document_is_authentic),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
            )

            Spacer(modifier = Modifier.height(40.dp))
            SelectableDocumentType(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(SharedRes.strings.bvn_display_text),
                onClick = {}
            )

            Spacer(modifier = Modifier.height(20.dp))
            SelectableDocumentType(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(SharedRes.strings.nin_display_text),
                onClick = {}
            )

            Spacer(modifier = Modifier.height(40.dp))
            TextButton(onClick = {}) {
                Text(stringResource(SharedRes.strings.i_dont_have_these))
            }
        }
    }
}

@Composable
private fun SelectableDocumentType(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier,
        onClick = onClick,
        shape = Shapes().small,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Icon(
//                painter = painterResource(id = iconResource),
//                contentDescription = "",
//                modifier = Modifier.size(24.dp),
//            )

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