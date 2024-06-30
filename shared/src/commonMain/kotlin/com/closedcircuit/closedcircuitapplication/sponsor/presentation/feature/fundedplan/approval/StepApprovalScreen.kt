package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.approval

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.PlanImage
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf


internal class StepApprovalScreen(private val stepID: ID) : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<StepApprovalViewModel> { parametersOf(stepID) }

        ScreenContent(viewModel.state.value, goBack = navigator::pop)
    }

    @Composable
    private fun ScreenContent(state: StepApprovalUiState, goBack: () -> Unit) {
        BaseScaffold(
            topBar = {
                DefaultAppBar(
                    mainAction = goBack,
                    title = stringResource(SharedRes.strings.step_proofs_label)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                when (state) {
                    is StepApprovalUiState.Content -> Body(state)
                    is StepApprovalUiState.Error -> {}
                    StepApprovalUiState.Loading -> BackgroundLoader()
                }
            }
        }
    }

    @Composable
    private fun Body(state: StepApprovalUiState.Content) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(state.proofItems) {
                ProofItem(modifier = Modifier.fillMaxWidth(), item = it)
            }
        }
    }

    @Composable
    private fun ProofItem(modifier: Modifier, item: ProofItem) {
        Column(modifier) {
            val commonModifier = remember { Modifier.padding(horizontal = horizontalScreenPadding) }
            HeaderText(
                modifier = commonModifier,
                text = stringResource(SharedRes.strings.budget_label)
            )

            Spacer(Modifier.height(8.dp))
            BodyText(modifier = commonModifier, text = item.name)

            Spacer(Modifier.height(20.dp))
            HeaderText(
                modifier = commonModifier,
                text = stringResource(SharedRes.strings.image_description_label)
            )

            Spacer(Modifier.height(8.dp))
            BodyText(modifier = commonModifier, text = item.description)

            Spacer(Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = horizontalScreenPadding)
            ) {
                item.images.forEach {
                    PlanImage(
                        modifier = Modifier.size(200.dp, 152.dp),
                        imageUrl = it,
                        shape = Shapes().small
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            Button(onClick = {}, shape = Shapes().small) {
                Text(stringResource(SharedRes.strings.approve_label))
            }
        }
    }

    @Composable
    private fun HeaderText(modifier: Modifier, text: String) {
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}