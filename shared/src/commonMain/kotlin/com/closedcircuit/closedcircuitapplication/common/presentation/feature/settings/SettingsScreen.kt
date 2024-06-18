package com.closedcircuit.closedcircuitapplication.common.presentation.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.BeneficiaryBottomTabs
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.util.conditional
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.SponsorBottomTabs
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent


@OptIn(ExperimentalMaterial3Api::class)
internal class SettingsScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<SettingsViewModel>()

        viewModel.resultChannel.receiveAsFlow().observeWithScreen {
            when (it) {
                is SettingResult.ChangeActiveProfileSuccess -> {
                    if (it.newProfile == ProfileType.BENEFICIARY)
                        navigator.replaceAll(BeneficiaryBottomTabs())
                    else navigator.replaceAll(SponsorBottomTabs())
                }
            }
        }

        ScreenContent(
            state = viewModel.state.collectAsState().value,
            onEvent = viewModel::onEvent,
            goBack = navigator::pop
        )
    }

    @Composable
    private fun ScreenContent(
        state: SettingsUiState,
        onEvent: (SettingsUiEvent) -> Unit,
        goBack: () -> Unit
    ) {
        BaseScaffold(
            topBar = {
                DefaultAppBar(
                    title = stringResource(SharedRes.strings.settings_label),
                    mainAction = goBack
                )
            }
        ) { innerPadding ->
            when (state) {
                is SettingsUiState.Content -> Body(
                    innerPadding = innerPadding,
                    state = state,
                    onEvent = onEvent
                )

                SettingsUiState.Loading -> BackgroundLoader()
            }
        }
    }

    @Composable
    private fun Body(
        innerPadding: PaddingValues,
        state: SettingsUiState.Content,
        onEvent: (SettingsUiEvent) -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = horizontalScreenPadding,
                    vertical = verticalScreenPadding
                )
        ) {
            SecuritySection()

            Spacer(Modifier.height(40.dp))
            NotificationsSection()

            Spacer(Modifier.height(40.dp))
            DisplaySection()

            Spacer(Modifier.height(40.dp))
            UserProfileSection(
                activeProfile = state.activeProfile,
                profileTypes = state.profileTypes,
                onEvent = onEvent
            )
        }
    }

    @Composable
    private fun SecuritySection() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SectionTitle(stringResource(SharedRes.strings.security_label))
            SectionItem(
                icon = painterResource(SharedRes.images.ic_fingerprint),
                text = stringResource(SharedRes.strings.enable_touch_id_to_sign_in_label),
                trailingIcon = { Switch(checked = false, onCheckedChange = {}) }
            )

            SectionItem(
                icon = painterResource(SharedRes.images.ic_phone_lock),
                text = stringResource(SharedRes.strings.enable_auto_lock_security_label),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            )

            SectionItem(
                icon = painterResource(SharedRes.images.ic_key),
                text = stringResource(SharedRes.strings.two_factor_authentication_label),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            )

            SectionItem(
                icon = painterResource(SharedRes.images.ic_lock),
                text = stringResource(SharedRes.strings.change_password_label),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            )
        }
    }

    @Composable
    private fun NotificationsSection() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SectionTitle(stringResource(SharedRes.strings.notifications))
            SectionItem(
                icon = painterResource(SharedRes.images.ic_notification),
                text = stringResource(SharedRes.strings.allow_notification_label),
                trailingIcon = { Switch(checked = false, onCheckedChange = {}) }
            )

            SectionItem(
                icon = rememberVectorPainter(Icons.Outlined.Email),
                text = stringResource(SharedRes.strings.email_notifications_label),
                trailingIcon = { Switch(checked = false, onCheckedChange = {}) }
            )
        }
    }

    @Composable
    private fun DisplaySection() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SectionTitle(stringResource(SharedRes.strings.display_label))
            SectionItem(
                icon = painterResource(SharedRes.images.ic_currency),
                text = stringResource(SharedRes.strings.currency_label),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            )

            SectionItem(
                icon = rememberVectorPainter(Icons.Outlined.Email),
                text = stringResource(SharedRes.strings.enable_prompts_label),
                trailingIcon = { Switch(checked = false, onCheckedChange = {}) }
            )
        }
    }

    @Composable
    private fun UserProfileSection(
        activeProfile: ProfileType,
        profileTypes: ImmutableList<ProfileType>,
        onEvent: (SettingsUiEvent) -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            var expanded by remember { mutableStateOf(false) }
            val commonModifier = Modifier.fillMaxWidth()
            val smallShape = remember { Shapes().small }
            val mediumShape = remember { Shapes().medium }

            SectionTitle(stringResource(SharedRes.strings.user_profile_label))
            Box(modifier = commonModifier) {
                ExposedDropdownMenuBox(
                    modifier = commonModifier.clip(mediumShape),
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    SectionItem(
                        icon = painterResource(SharedRes.images.ic_persons_circled),
                        text = activeProfile.displayText,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.exposedDropdownSize()
                            .background(color = Color.White, shape = smallShape),
                    ) {
                        profileTypes.forEach { item ->
                            DropdownMenuItem(
                                modifier = commonModifier.padding(horizontal = 8.dp)
                                    .conditional(
                                        condition = activeProfile == item,
                                        ifTrue = {
                                            Modifier.background(
                                                color = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                                    Elevation.Level1
                                                ),
                                                shape = mediumShape
                                            )
                                        }
                                    ),
                                text = { Text(text = item.displayText) },
                                onClick = {
                                    onEvent(SettingsUiEvent.ProfileTypeChange(item))
                                    expanded = false
                                },
                                trailingIcon = {
                                    if (activeProfile == item) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SectionTitle(text: String) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }

    @Composable
    private fun SectionItem(
        modifier: Modifier = Modifier,
        icon: Painter,
        text: String,
        trailingIcon: @Composable () -> Unit
    ) {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.clip(Shapes().large)
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(Elevation.Level1))
                    .padding(12.dp)
            ) {
                Icon(painter = icon, contentDescription = null, modifier = Modifier.size(24.dp))
            }

            Spacer(Modifier.width(12.dp))
            Text(text = text, modifier = Modifier.weight(1f))

            Spacer(Modifier.width(12.dp))
            trailingIcon()
        }
    }
}