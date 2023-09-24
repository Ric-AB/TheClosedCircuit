package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.planlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.ProgressIndicator
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent

internal object PlanListScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ScreenContent(goBack = navigator::pop)
    }
}

@Composable
private fun ScreenContent(goBack: () -> Unit) {
    BaseScaffold(
        topBar = { DefaultAppBar(stringResource(SharedRes.strings.plans)) },
        floatingActionButton = { PlansExtendedFab { } }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = defaultHorizontalScreenPadding)
        ) {
            items(10) {
                PlanCard(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun PlanCard(modifier: Modifier) {
    OutlinedCard(modifier = modifier.padding(horizontal = 12.dp, vertical = 16.dp)) {
        Text(
            text = "E-commerce",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint. Velit officia consequat duis enim velit mollit. Exercitation veniam consequat...",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))
        ProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = 0.4F,
            displayText = stringResource(SharedRes.strings.percentage_funds_raised, "40")
        )

        Spacer(modifier = Modifier.height(16.dp))
        ProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = 0.5F,
            displayText = stringResource(SharedRes.strings.percentage_task_completed, "50")
        )
    }
}

@Composable
private fun PlansExtendedFab(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        text = { Text("New plan") },
        icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) }
    )
}
