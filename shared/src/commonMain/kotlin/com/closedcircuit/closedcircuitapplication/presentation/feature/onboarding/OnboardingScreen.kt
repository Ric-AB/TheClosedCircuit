@file:OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)

package com.closedcircuit.closedcircuitapplication.presentation.feature.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login.LoginScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


internal object OnboardingScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel by inject<OnboardingViewModel>()

        ScreenContent(
            navigateToWelcomeScreen = {
                viewModel.onEvent(OnboardingEvent.OnboardingFinished)
                navigator.replace(WelcomeScreen)
            },
            navigateToLoginScreen = {
                viewModel.onEvent(OnboardingEvent.OnboardingFinished)
                navigator.replace(LoginScreen)
            }
        )
    }
}

@Composable
private fun ScreenContent(
    navigateToWelcomeScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit
) {
    val onboardingPages = listOf(
        OnboardingPage.First,
        OnboardingPage.Second,
        OnboardingPage.Third,
        OnboardingPage.Fourth,
    )

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                pageCount = onboardingPages.size,
                modifier = Modifier.weight(10F)
            ) { page ->
                PagerScreen(onboardingPages[page])
            }

            HorizontalIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally).weight(1F),
                pagerState = pagerState,
                pageCount = onboardingPages.size
            )

            ActionButtons(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp)
                    .weight(1F),
                isLastPage = pagerState.currentPage == onboardingPages.lastIndex,
                onSkipClick = navigateToLoginScreen,
                onNextClick = {
                    scope.launch {
                        if (pagerState.canScrollForward)
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                onFinishClick = navigateToWelcomeScreen
            )
        }
    }
}

@Composable
private fun ActionButtons(
    modifier: Modifier,
    isLastPage: Boolean,
    onSkipClick: () -> Unit,
    onNextClick: () -> Unit,
    onFinishClick: () -> Unit
) {

    AnimatedContent(
        targetState = isLastPage,
        transitionSpec = { fadeIn(tween()) with fadeOut(tween()) },
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) { isLastPageState ->
        if (isLastPageState) {
            Button(
                onClick = onFinishClick,
                modifier = Modifier.fillMaxWidth(0.9F),
                shape = Shapes().small
            ) {
                Text(stringResource(SharedRes.strings.get_started))

                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Rounded.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onSkipClick) {
                    Text(stringResource(SharedRes.strings.skip))
                }

                Button(onNextClick) {
                    Text(stringResource(SharedRes.strings.next))

                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

}

@Composable
private fun PagerScreen(page: OnboardingPage) {
    OnboardingBackground {
        Column {
            Image(
                painterResource(page.image),
                contentDescription = null,
                modifier = Modifier.size(410.dp, 350.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(page.title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(page.description),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun OnboardingBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painterResource(SharedRes.images.onboarding_background_lines),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )

        content()
    }
}

@Composable
private fun HorizontalIndicator(
    modifier: Modifier,
    pagerState: PagerState,
    pageCount: Int,
    indicatorWidth: Dp = 8.dp,
    activeIndicatorWidth: Dp = indicatorWidth * 3
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { iteration ->

            val width by animateDpAsState(
                targetValue = if (pagerState.currentPage == iteration) {
                    activeIndicatorWidth
                } else {
                    indicatorWidth
                },
                label = "width",
                animationSpec = tween(300, easing = EaseInOut)
            )
            val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary
            else Color.Gray

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
                    .size(width = width, height = 8.dp)
            )
        }
    }
}