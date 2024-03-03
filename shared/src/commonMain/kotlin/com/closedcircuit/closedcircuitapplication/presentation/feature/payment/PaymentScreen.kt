package com.closedcircuit.closedcircuitapplication.presentation.feature.payment

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.util.observerWithScreen
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewState
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent


internal class PaymentScreen(private val url: String) : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val webViewState = rememberWebViewState(url)
        val viewModel = getScreenModel<PaymentViewModel>()
        viewModel.resultChannel.receiveAsFlow().observerWithScreen {
            when (it) {
                is PaymentResult.Error -> {}
                PaymentResult.Success -> navigator.pop()
            }
        }

        ScreenContent(webViewState = webViewState)
    }

    @Composable
    private fun ScreenContent(webViewState: WebViewState) {
        BaseScaffold { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
            ) {
                WebView(webViewState)
            }
        }
    }
}