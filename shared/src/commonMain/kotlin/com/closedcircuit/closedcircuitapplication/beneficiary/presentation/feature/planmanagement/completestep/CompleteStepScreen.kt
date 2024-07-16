package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.completestep

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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.uploadproof.UploadProofScreen
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf


internal class CompleteStepScreen(
    private val planID: ID,
    private val stepID: ID
) : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<CompleteStepViewModel> { parametersOf(planID, stepID) }
        val state = viewModel.state.value
        ScreenContent(
            state = state,
            goBack = navigator::pop,
            navigateToUploadScreen = { navigator.push(UploadProofScreen(it)) }
        )
    }

    @Composable
    private fun ScreenContent(
        state: CompleteStepUiState,
        goBack: () -> Unit,
        navigateToUploadScreen: (ID) -> Unit
    ) {
        BaseScaffold(
            topBar = { DefaultAppBar(mainAction = goBack) }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)

            ) {
                when (state) {
                    is CompleteStepUiState.Content -> Body(state, navigateToUploadScreen)
                    is CompleteStepUiState.Error -> {
                        Text(state.message)
                    }

                    CompleteStepUiState.Loading -> BackgroundLoader()
                }
            }
        }
    }

    @Composable
    private fun Body(state: CompleteStepUiState.Content, navigateToUploadScreen: (ID) -> Unit) {
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
        ) {
            TitleText(stringResource(SharedRes.strings.step_proofs_label))

            Spacer(Modifier.height(4.dp))
            BodyText(stringResource(SharedRes.strings.step_proof_message_label))

            Spacer(Modifier.height(24.dp))
            state.budgetItems.forEach {
                BudgetItem(budget = it, onClick = { navigateToUploadScreen(it.id) })
                Spacer(Modifier.height(20.dp))
            }

            Spacer(Modifier.height(40.dp))
            DefaultButton(onClick = {}) {
                Text(stringResource(SharedRes.strings.complete_step_label))
            }
        }
    }

    @Composable
    private fun BudgetItem(budget: BudgetItem, onClick: () -> Unit) {
        Card(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(SharedRes.images.ic_target_arrow),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(Modifier.width(12.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = budget.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(2.dp))
                    Text(budget.amount)

                    Text(
                        text = budget.uploadStatus,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }

                Spacer(Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }
    }
}