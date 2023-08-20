@file:OptIn(ExperimentalTime::class)

package com.closedcircuit.closedcircuitapplication.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.closedcircuit.closedcircuitapplication.core.timer
import com.moriatsushi.insetsx.statusBars
import kotlinx.coroutines.Job
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class MessageBarState {
    var success by mutableStateOf<String?>(null)
        private set
    var error by mutableStateOf<Exception?>(null)
        private set
    var successCallback: (() -> Unit)? by mutableStateOf(null)
        private set
    var errorCallback: (() -> Unit)? by mutableStateOf(null)
        private set
    internal var updated by mutableStateOf(false)
        private set

    fun addSuccess(message: String, callback: (() -> Unit)? = null) {
        error = null
        success = message
        successCallback = callback
        updated = !updated
    }

    fun addError(exception: Exception, callback: (() -> Unit)? = null) {
        success = null
        error = exception
        errorCallback = callback
        updated = !updated
    }

    fun addError(errorMessage: String, callback: (() -> Unit)? = null) {
        success = null
        error = Exception(errorMessage)
        errorCallback = callback
        updated = !updated
    }
}

@Composable
fun rememberMessageBarState(): MessageBarState {
    return remember { MessageBarState() }
}

enum class MessageBarPosition {
    TOP,
    BOTTOM
}

@Composable
fun ContentWithMessageBar(
    modifier: Modifier = Modifier,
    messageBarState: MessageBarState,
    position: MessageBarPosition = MessageBarPosition.TOP,
    visibilityDuration: Duration = 3.seconds,
    successIcon: ImageVector = Icons.Default.Check,
    errorIcon: ImageVector = Icons.Default.Warning,
    errorMaxLines: Int = 1,
    successMaxLines: Int = 1,
    contentBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    successContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    successContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    errorContainerColor: Color = MaterialTheme.colorScheme.errorContainer,
    errorContentColor: Color = MaterialTheme.colorScheme.onErrorContainer,
    enterAnimation: EnterTransition = expandVertically(
        animationSpec = tween(durationMillis = 300),
        expandFrom = if (position == MessageBarPosition.TOP)
            Alignment.Top else Alignment.Bottom
    ),
    exitAnimation: ExitTransition = shrinkVertically(
        animationSpec = tween(durationMillis = 300),
        shrinkTowards = if (position == MessageBarPosition.TOP)
            Alignment.Top else Alignment.Bottom
    ),
    verticalPadding: Dp = 12.dp,
    horizontalPadding: Dp = 12.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = contentBackgroundColor)
    ) {
        content()
        MessageBarComponent(
            messageBarState = messageBarState,
            position = position,
            visibilityDuration = visibilityDuration,
            successIcon = successIcon,
            errorIcon = errorIcon,
            errorMaxLines = errorMaxLines,
            successMaxLines = successMaxLines,
            successContainerColor = successContainerColor,
            successContentColor = successContentColor,
            errorContainerColor = errorContainerColor,
            errorContentColor = errorContentColor,
            enterAnimation = enterAnimation,
            exitAnimation = exitAnimation,
            verticalPadding = verticalPadding,
            horizontalPadding = horizontalPadding,
        )
    }
}

@Composable
private fun MessageBarComponent(
    messageBarState: MessageBarState,
    position: MessageBarPosition,
    visibilityDuration: Duration,
    successIcon: ImageVector,
    errorIcon: ImageVector,
    errorMaxLines: Int,
    successMaxLines: Int,
    successContainerColor: Color,
    successContentColor: Color,
    errorContainerColor: Color,
    errorContentColor: Color,
    enterAnimation: EnterTransition,
    exitAnimation: ExitTransition,
    verticalPadding: Dp,
    horizontalPadding: Dp,
) {
    var showMessageBar by remember { mutableStateOf(false) }
    var job: Job
    val scope = rememberCoroutineScope()
    val error by rememberUpdatedState(newValue = messageBarState.error?.message)
    val message by rememberUpdatedState(newValue = messageBarState.success)
    val successCallback by rememberUpdatedState(newValue = messageBarState.successCallback)
    val errorCallback by rememberUpdatedState(newValue = messageBarState.errorCallback)

    DisposableEffect(key1 = messageBarState.updated) {
        showMessageBar = true
        job = scope.timer(startDelay = visibilityDuration) {
            showMessageBar = false
            if (error != null) {
                errorCallback?.let { it() }
            }

            if (message != null) {
                successCallback?.let { it() }
            }
        }
        onDispose {
            job.cancel()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = if (position == MessageBarPosition.TOP)
            Arrangement.Top else Arrangement.Bottom
    ) {
        AnimatedVisibility(
            visible = messageBarState.error != null && showMessageBar
                    || messageBarState.success != null && showMessageBar,
            enter = enterAnimation,
            exit = exitAnimation
        ) {
            MessageBar(
                message = message,
                error = error,
                successIcon = successIcon,
                errorIcon = errorIcon,
                errorMaxLines = errorMaxLines,
                successMaxLines = successMaxLines,
                successContainerColor = successContainerColor,
                successContentColor = successContentColor,
                errorContainerColor = errorContainerColor,
                errorContentColor = errorContentColor,
                verticalPadding = verticalPadding,
                horizontalPadding = horizontalPadding,
            )
        }
    }
}

@Composable
private fun MessageBar(
    message: String?,
    error: String?,
    successIcon: ImageVector,
    errorIcon: ImageVector,
    errorMaxLines: Int,
    successMaxLines: Int,
    successContainerColor: Color,
    successContentColor: Color,
    errorContainerColor: Color,
    errorContentColor: Color,
    verticalPadding: Dp,
    horizontalPadding: Dp,
) {
    val clipboardManager = LocalClipboardManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (error != null) errorContainerColor
                else successContainerColor
            )
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
//            .padding(vertical = verticalPadding)
            .padding(horizontal = horizontalPadding)
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(4f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector =
                if (error != null) errorIcon
                else successIcon,
                contentDescription = "Message Bar Icon",
                tint = if (error != null) errorContentColor
                else successContentColor
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                modifier = Modifier.testTag(tag = "MESSAGE_BAR_TEXT"),
                text = message ?: (error ?: "Unknown"),
                color = if (error != null) errorContentColor
                else successContentColor,
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (error != null) errorMaxLines else successMaxLines
            )
        }
        if (error != null) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    modifier = Modifier.testTag(tag = "COPY_BUTTON"),
                    onClick = {
                        clipboardManager.setText(AnnotatedString(text = "$error"))
                    },
                    contentPadding = PaddingValues(vertical = 0.dp)
                ) {
                    Text(
                        text = "Copy",
                        color = errorContentColor,
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}
