@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.presentation.feature.profile.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Shapes
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.Country
import com.closedcircuit.closedcircuitapplication.domain.model.Email
import com.closedcircuit.closedcircuitapplication.domain.model.Name
import com.closedcircuit.closedcircuitapplication.domain.model.PhoneNumber
import com.closedcircuit.closedcircuitapplication.domain.model.KycStatus
import com.closedcircuit.closedcircuitapplication.domain.user.User
import com.closedcircuit.closedcircuitapplication.presentation.component.Avatar
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.feature.profile.edit.EditProfileScreen
import com.closedcircuit.closedcircuitapplication.presentation.feature.profile.profileverification.ProfileVerificationScreen
import com.closedcircuit.closedcircuitapplication.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class ProfileScreen : Screen, KoinComponent {
    private val viewModel: ProfileViewModel by inject()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val messageBarState = rememberMessageBarState()
        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val uiState by viewModel.state.collectAsState()
        var isSheetVisible by remember { mutableStateOf(false) }

        ScreenContent(
            messageBarState = messageBarState,
            uiState = uiState,
            bottomSheetState = bottomSheetState,
            sheetExpanded = isSheetVisible,
            sheetExpandedChange = { isSheetVisible = it },
            navigateToEditProfileScreen = { navigator.push(EditProfileScreen(it)) },
            navigateToProfileVerificationScreen = {
                uiState?.let { navigator.push(ProfileVerificationScreen(it.user.email)) }
            },
            navigateToKycScreen = {}
        )
    }
}

@Composable
private fun ScreenContent(
    messageBarState: MessageBarState,
    uiState: ProfileUIState?,
    bottomSheetState: SheetState,
    sheetExpanded: Boolean,
    sheetExpandedChange: (Boolean) -> Unit,
    navigateToEditProfileScreen: (User) -> Unit,
    navigateToProfileVerificationScreen: () -> Unit,
    navigateToKycScreen: () -> Unit
) {
    BaseScaffold(
        messageBarState = messageBarState,
        topBar = {
            DefaultAppBar(
                title = stringResource(SharedRes.strings.profile),
                mainIcon = null
            )
        }
    ) { innerPadding ->

        uiState?.let {
            val user = it.user

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = horizontalScreenPadding)
            ) {
                ProfileHeader(
                    modifier = Modifier.fillMaxWidth(),
                    firstName = user.firstName,
                    avatar = user.avatar,
                    onInfoClick = { sheetExpandedChange(true) }
                )

                Spacer(modifier = Modifier.height(32.dp))
                PersonalData(
                    modifier = Modifier.fillMaxWidth(),
                    fullName = user.fullName,
                    email = user.email,
                    phoneNumber = user.phoneNumber,
                    country = user.country,
                    onEditClick = { navigateToEditProfileScreen(user) }
                )
            }

            ProfileModalBottomSheet(
                modifier = Modifier.padding(horizontal = 8.dp),
                bottomSheetState = bottomSheetState,
                isVisible = sheetExpanded,
                isEmailVerified = user.isVerified,
                documentStatus = user.kycStatus,
                phoneNumberStatus = user.phoneNumberStatus,
                closeModal = { sheetExpandedChange(false) },
                navigateToProfileVerificationScreen = navigateToProfileVerificationScreen,
                navigateToKycScreen = navigateToKycScreen
            )
        }
    }
}

@Composable
private fun ProfileHeader(
    modifier: Modifier = Modifier,
    firstName: Name,
    avatar: Avatar,
    onInfoClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(SharedRes.strings.hello_user, firstName.value),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = Shapes().extraSmall
                ).padding(8.dp)
                    .clickable(enabled = true, onClick = onInfoClick)
            ) {
                Text(
                    text = stringResource(SharedRes.strings.account_status),
                    style = MaterialTheme.typography.bodySmall,
                )

                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Avatar(
            avatar = avatar,
            size = DpSize(90.dp, 90.dp)
        )
    }
}

@Composable
private fun PersonalData(
    modifier: Modifier = Modifier,
    fullName: Name,
    email: Email,
    phoneNumber: PhoneNumber,
    country: Country,
    onEditClick: () -> Unit
) {
    Column(
        modifier = modifier,
    ) {
        SectionHeader(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(SharedRes.strings.personal_data),
            onEditClick = onEditClick
        )

        Spacer(modifier = Modifier.height(4.dp))
        ScreenCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                InfoItem(
                    labelRes = SharedRes.strings.full_name,
                    value = fullName.value,
                    icon = Icons.Outlined.Person
                )

                InfoItem(
                    labelRes = SharedRes.strings.email,
                    value = email.value,
                    icon = Icons.Outlined.Email
                )

                InfoItem(
                    labelRes = SharedRes.strings.phone_number,
                    value = phoneNumber.value,
                    icon = Icons.Outlined.Phone
                )

                InfoItem(
                    labelRes = SharedRes.strings.country,
                    value = country.value,
                    icon = Icons.Outlined.LocationOn
                )
            }
        }
    }


}

@Composable
private fun SectionHeader(modifier: Modifier, text: String, onEditClick: () -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Normal
        )

        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "edit profile",
                modifier = Modifier.size(18.dp),
                tint = Color.Gray
            )
        }
    }
}

@Composable
private fun InfoItem(labelRes: StringResource, value: String, icon: ImageVector) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight(500),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ScreenCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    OutlinedCard(
        modifier = modifier,
        shape = Shapes().small,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(Elevation.Level1)
        )
    ) {
        content()
    }
}

@Composable
private fun ProfileModalBottomSheet(
    modifier: Modifier = Modifier,
    bottomSheetState: SheetState,
    isVisible: Boolean,
    isEmailVerified: Boolean,
    documentStatus: KycStatus,
    phoneNumberStatus: KycStatus,
    closeModal: () -> Unit,
    navigateToProfileVerificationScreen: () -> Unit,
    navigateToKycScreen: () -> Unit
) {
    val mapStatusToDisplayValues: (KycStatus) -> Pair<String, ImageVector> = {
        when (it) {
            KycStatus.NOT_STARTED -> Pair("Not started", Icons.Outlined.Info)
            KycStatus.PENDING -> Pair("Pending", Icons.Outlined.Info)
            KycStatus.VERIFIED -> Pair("Verified", Icons.Outlined.Info)
            KycStatus.FAILED -> Pair("Failed", Icons.Outlined.Info)
        }
    }

    val documentStatusValues =
        remember(documentStatus) { mapStatusToDisplayValues(documentStatus) }
    val phoneNumberStatusValues =
        remember(phoneNumberStatus) { mapStatusToDisplayValues(phoneNumberStatus) }


    @Composable
    fun Item(label: String, status: String, icon: ImageVector, onClick: (() -> Unit)? = null) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .then(
                    if (onClick != null) Modifier.clickable(enabled = true, onClick = onClick)
                    else Modifier
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = label, style = MaterialTheme.typography.bodySmall)
                Text(text = status, style = MaterialTheme.typography.bodyMedium)
            }

            Icon(imageVector = icon, contentDescription = null)
        }
    }

    if (isVisible) {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = closeModal,
            sheetState = bottomSheetState,
            shape = Shapes().small.copy(
                bottomStart = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp)
            ),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = stringResource(SharedRes.strings.your_account),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )

                val emailStatus = if (isEmailVerified) stringResource(SharedRes.strings.verified)
                else stringResource(SharedRes.strings.unverified)
                Item(
                    label = stringResource(SharedRes.strings.email_verification),
                    status = emailStatus,
                    icon = Icons.Outlined.Done,
                    onClick = if (!isEmailVerified) navigateToProfileVerificationScreen else null
                )

                Divider()
                Item(
                    label = stringResource(SharedRes.strings.document_verification),
                    status = documentStatusValues.first,
                    icon = documentStatusValues.second,
                    onClick = navigateToKycScreen
                )

                Divider()
                Item(
                    label = stringResource(SharedRes.strings.phone_number_verification),
                    status = phoneNumberStatusValues.first,
                    icon = phoneNumberStatusValues.second,
                    onClick = navigateToKycScreen
                )
            }
        }
    }

}
