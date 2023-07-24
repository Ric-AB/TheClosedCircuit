@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource


object WelcomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ScreenContent({}, {})
    }
}


@Composable
private fun ScreenContent(onLoginClick: () -> Unit, onCreateAccountClick: () -> Unit) {
    val typography = MaterialTheme.typography

    Scaffold { paddingValues ->
        OnboardingBackground {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 12.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = stringResource(SharedRes.strings.welcome_to_the_closed_circuit),
                    style = typography.titleLarge,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(SharedRes.strings.welcome_quote),
                    style = typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))
                Image(
                    painter = painterResource(SharedRes.images.welocome_hand_illurstration),
                    modifier = Modifier.size(135.dp, 225.dp),
                    contentDescription = "welcome to the closed circuit"
                )

                val largeButtonShape = Shapes().small
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth(0.9F),
                    shape = largeButtonShape,
                ) {
                    Text(text = stringResource(SharedRes.strings.login))
                }

                Spacer(modifier = Modifier.height(20.dp))
                OutlinedButton(
                    onClick = onCreateAccountClick,
                    modifier = Modifier.fillMaxWidth(0.9F),
                    shape = largeButtonShape,
                ) {
                    Text(text = stringResource(SharedRes.strings.create_account))
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(SharedRes.strings.terms_of_service_quote),
                    style = typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 10.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun Dummy() {

}