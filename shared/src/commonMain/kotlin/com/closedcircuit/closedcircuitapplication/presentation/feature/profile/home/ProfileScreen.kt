package com.closedcircuit.closedcircuitapplication.presentation.feature.profile.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.closedcircuit.closedcircuitapplication.domain.user.User
import com.closedcircuit.closedcircuitapplication.presentation.component.Avatar
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.feature.profile.edit.EditProfileScreen
import com.closedcircuit.closedcircuitapplication.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal object ProfileScreen : Screen, KoinComponent {
    private val viewModel: ProfileViewModel by inject()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val messageBarState = rememberMessageBarState()
        val uiState by viewModel.state.collectAsState()
        ScreenContent(
            messageBarState = messageBarState,
            uiState = uiState,
            navigateToEditProfileScreen = { navigator.push(EditProfileScreen(it)) }
        )
    }
}

@Composable
private fun ScreenContent(
    messageBarState: MessageBarState,
    uiState: ProfileUIState?,
    navigateToEditProfileScreen: (User) -> Unit
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
                    .padding(horizontal = defaultHorizontalScreenPadding)
            ) {
                ProfileHeader(
                    modifier = Modifier.fillMaxWidth(),
                    firstName = user.firstName,
                    avatar = user.avatar
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
        }
    }
}

@Composable
private fun ProfileHeader(
    modifier: Modifier = Modifier,
    firstName: Name,
    avatar: Avatar
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

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = Shapes().extraSmall
                )
            ) {
                Text(
                    text = stringResource(SharedRes.strings.profile_status),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp)
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
                InfoItem(SharedRes.strings.full_name, fullName.value)
                InfoItem(SharedRes.strings.email, email.value)
                InfoItem(SharedRes.strings.phone_number, phoneNumber.value)
                InfoItem(SharedRes.strings.country, country.value)
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
private fun InfoItem(labelRes: StringResource, value: String) {
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

@Composable
private fun ScreenCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    OutlinedCard(
        modifier = modifier,
        shape = Shapes().small,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme
                .colorScheme.surfaceColorAtElevation(Elevation.Level1)
        )
    ) {
        content()
    }
}
