package com.closedcircuit.closedcircuitapplication.common.presentation.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.BeneficiaryBottomTabs
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.changepassword.ChangePasswordScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.AuthNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.ScreenKeys
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.findNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.findRootNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.util.conditional
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.common.util.orFalse
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
        val rootNavigator = findRootNavigator(LocalNavigator.currentOrThrow)
        val navigator =
            findNavigator(ScreenKeys.PROTECTED_NAVIGATOR, LocalNavigator.currentOrThrow)

        val messageBarState = rememberMessageBarState()
        val viewModel = getScreenModel<SettingsViewModel>()
        val uiState = viewModel.state.collectAsState().value
        val onEvent = viewModel::onEvent

        viewModel.resultChannel.receiveAsFlow().observeWithScreen {
            when (it) {
                is SettingResult.ChangeActiveProfileSuccess -> {
                    if (it.newProfile == ProfileType.BENEFICIARY)
                        navigator.replaceAll(BeneficiaryBottomTabs())
                    else navigator.replaceAll(SponsorBottomTabs())
                }

                is SettingResult.DeleteAccountError -> messageBarState.addError(it.message)
                SettingResult.DeleteAccountSuccess -> rootNavigator.replaceAll(AuthNavigator(false))
            }
        }

        BaseScaffold(
            messageBarState = messageBarState,
            showLoadingDialog = (uiState as? SettingsUiState.Content)?.loading.orFalse(),
            topBar = {
                DefaultAppBar(
                    title = stringResource(SharedRes.strings.settings_label),
                    mainAction = navigator::pop
                )
            }
        ) { innerPadding ->
            when (uiState) {
                is SettingsUiState.Content -> Body(
                    innerPadding = innerPadding,
                    uiState = uiState,
                    onEvent = onEvent,
                    navigateToChangePassword = { navigator.push(ChangePasswordScreen()) }
                )

                SettingsUiState.Loading -> BackgroundLoader()
            }
        }
    }

    @Composable
    private fun Body(
        innerPadding: PaddingValues,
        uiState: SettingsUiState.Content,
        onEvent: (SettingsUiEvent) -> Unit,
        navigateToChangePassword: () -> Unit
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
            SecuritySection(navigateToChangePassword)

            Spacer(Modifier.height(40.dp))
            NotificationsSection()

            Spacer(Modifier.height(40.dp))
            DisplaySection()

            Spacer(Modifier.height(40.dp))
            UserProfileSection(
                activeProfile = uiState.activeProfile,
                profileTypes = uiState.profileTypes,
                onEvent = onEvent
            )

            Spacer(Modifier.height(40.dp))
            AccountSection(onEvent)
        }
    }

    @Composable
    private fun SecuritySection(navigateToChangePassword: () -> Unit) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            var touchIdEnable by remember { mutableStateOf(false) }
            SectionTitle(stringResource(SharedRes.strings.security_label))
            SectionItem(
                icon = painterResource(SharedRes.images.ic_fingerprint),
                text = stringResource(SharedRes.strings.enable_touch_id_to_sign_in_label),
                trailingIcon = {
                    Switch(
                        checked = touchIdEnable,
                        onCheckedChange = { touchIdEnable = it }
                    )
                }
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
                },
                modifier = Modifier.clickable(onClick = navigateToChangePassword)
            )
        }
    }

    @Composable
    private fun NotificationsSection() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            var pushNotificationEnabled by remember { mutableStateOf(false) }
            var emailNotificationEnabled by remember { mutableStateOf(false) }

            SectionTitle(stringResource(SharedRes.strings.notifications))
            SectionItem(
                icon = painterResource(SharedRes.images.ic_notification),
                text = stringResource(SharedRes.strings.allow_notification_label),
                trailingIcon = {
                    Switch(
                        checked = pushNotificationEnabled,
                        onCheckedChange = { pushNotificationEnabled = it }
                    )
                }
            )

            SectionItem(
                icon = rememberVectorPainter(Icons.Outlined.Email),
                text = stringResource(SharedRes.strings.email_notifications_label),
                trailingIcon = {
                    Switch(
                        checked = emailNotificationEnabled,
                        onCheckedChange = { emailNotificationEnabled = it }
                    )
                }
            )
        }
    }

    @Composable
    private fun DisplaySection() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            var promptEnabled by remember { mutableStateOf(false) }
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
                trailingIcon = {
                    Switch(
                        checked = promptEnabled,
                        onCheckedChange = { promptEnabled = it }
                    )
                }
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
    private fun AccountSection(onEvent: (SettingsUiEvent) -> Unit) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            var showDialog1 by remember { mutableStateOf(false) }
            var showDialog2 by remember { mutableStateOf(false) }
            var showDialog3 by remember { mutableStateOf(false) }

            SectionTitle(stringResource(SharedRes.strings.account_label))
            SectionItem(
                icon = painterResource(SharedRes.images.ic_outline_delete),
                text = stringResource(SharedRes.strings.delete_account_label),
                trailingIcon = {},
                modifier = Modifier.clickable { showDialog1 = true }
            )

            DeleteDialog(
                visible = showDialog1,
                message = stringResource(SharedRes.strings.delete_account_prompt_one),
                onDismiss = { showDialog1 = false },
                onConfirm = { showDialog2 = true }
            )

            DeleteDialog(
                visible = showDialog2,
                message = stringResource(SharedRes.strings.delete_account_prompt_two),
                onDismiss = { showDialog2 = false },
                onConfirm = { showDialog3 = true }
            )

            DeleteDialog(
                visible = showDialog3,
                message = stringResource(SharedRes.strings.delete_account_prompt_three),
                onDismiss = { showDialog3 = false },
                onConfirm = { onEvent(SettingsUiEvent.DeleteAccount) }
            )
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

    @Composable
    private fun DeleteDialog(
        visible: Boolean,
        message: String,
        onDismiss: () -> Unit,
        onConfirm: () -> Unit
    ) {
        if (visible) {
            Dialog(
                onDismissRequest = onDismiss,
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.background(
                        color = Color.White,
                        shape = MaterialTheme.shapes.medium
                    ).padding(24.dp)
                ) {
                    Text(
                        text = stringResource(SharedRes.strings.delete_account_label),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.align(Alignment.Start),
                        lineHeight = 15.sp,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(24.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val buttonShape = MaterialTheme.shapes.small
                        Button(
                            onClick = onDismiss,
                            shape = buttonShape,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text(text = stringResource(SharedRes.strings.cancel_label))
                        }

                        Spacer(Modifier.width(16.dp))
                        Button(
                            onClick = { onDismiss(); onConfirm() },
                            shape = buttonShape,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(text = stringResource(SharedRes.strings.continue_label))
                        }
                    }
                }
            }
        }
    }
}
