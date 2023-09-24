@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.closedcircuit.closedcircuitapplication.core.validation.PasswordValidator
import com.closedcircuit.closedcircuitapplication.presentation.component.icon.rememberVisibility
import com.closedcircuit.closedcircuitapplication.presentation.component.icon.rememberVisibilityOff
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DefaultOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: String? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = Shapes().medium,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
        focusedContainerColor = MaterialTheme.colorScheme.inverseOnSurface
    )
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = {
            if (label != null) {
                Text(text = label)
            }
        },
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors
    )
}

@Composable
fun PasswordOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    label: String? = null,
    placeholder: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errors: List<String> = emptyList(),
    singleLine: Boolean = true,
    showCriteria: Boolean = false,
    showPassword: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape = Shapes().medium,
) {
    Column {
        DefaultOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            label = label,
            placeholder = placeholder,
            supportingText = {
                if (showCriteria) {
                    Column {
                        PasswordValidator.criteriaMessage.values.map { criteria ->
                            Text(
                                text = criteria,
                                color = if (errors.contains(criteria)) MaterialTheme.colorScheme.error else Color.Gray
                            )
                        }
                    }
                } else if (supportingText != null) {
                    supportingText()
                }
            },
            isError = isError,
            singleLine = singleLine,
            shape = shape,
            trailingIcon = {
                IconButton(onClick = { onPasswordVisibilityChange(!showPassword) }) {
                    Icon(
                        imageVector = if (showPassword) rememberVisibilityOff() else rememberVisibility(),
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            ),
            keyboardActions = keyboardActions,
            maxLines = 1
        )
    }

}

@Composable
fun OtpView(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    otpCode: String = "",
    itemCount: Int = 4,
    withBorders: Boolean = true,
    isError: Boolean = false,
    borderWidth: Dp = 2.dp,
    itemSpacing: Dp = 8.dp,
    itemWidth: Dp = 54.dp,
    itemHeight: Dp = 54.dp,
    itemBackground: Color = MaterialTheme.colorScheme.inverseOnSurface,
    onOtpTextChange: (String, Boolean) -> Unit
) {
    BasicTextField(
        modifier = modifier,
        value = otpCode,
        onValueChange = {
            if (it.length <= itemCount) {
                onOtpTextChange.invoke(it, it.length == itemCount)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        decorationBox = {
            Column {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    repeat(itemCount) { index ->
                        OtpItem(
                            index = index,
                            text = otpCode,
                            modifier = modifier,
                            textStyle = textStyle,
                            withBorder = withBorders,
                            borderWidth = borderWidth,
                            isError = isError,
                            itemWidth = itemWidth,
                            itemHeight = itemHeight,
                            itemBackground = itemBackground
                        )
                        Spacer(modifier = Modifier.width(itemSpacing))
                    }
                }

                if (isError) {
                    BodyText(
                        text = stringResource(SharedRes.strings.invalid_code_try_again),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
    )
}

@Composable
fun OtpItem(
    index: Int = 0,
    text: String = "",
    modifier: Modifier,
    textStyle: TextStyle,
    withBorder: Boolean,
    isError: Boolean,
    borderWidth: Dp,
    shape: Shape = Shapes().small,
    itemWidth: Dp,
    itemHeight: Dp,
    itemBackground: Color
) {

    val scope = rememberCoroutineScope()
    val (cursorSymbol, setCursorSymbol) = remember { mutableStateOf("") }

    val isFocused = text.length == index
    val char = when {
        index == text.length -> ""
        index > text.length -> ""
        else -> text[index].toString()
    }

    LaunchedEffect(key1 = cursorSymbol, isFocused) {
        if (isFocused) {
            scope.launch {
                delay(350)
                setCursorSymbol(if (cursorSymbol.isEmpty()) "|" else "")
            }
        }
    }

    Box(
        modifier = Modifier
            .width(itemWidth)
            .height(itemHeight)
            .background(color = itemBackground, shape = shape)
            .border(
                if (withBorder) borderWidth else 0.dp,
                if (withBorder) {
                    when {
                        isError -> MaterialTheme.colorScheme.error
                        isFocused -> MaterialTheme.colorScheme.primary
                        else -> Color.Transparent
                    }
                } else Color.Transparent,
                shape
            )
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isFocused) cursorSymbol else char,
            textAlign = TextAlign.Center,
            style = textStyle
        )
    }

}