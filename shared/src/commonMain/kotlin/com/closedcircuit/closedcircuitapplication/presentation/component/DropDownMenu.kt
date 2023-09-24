@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.collections.immutable.ImmutableList


@Composable
fun ExposedDropDownMenu(
    modifier: Modifier = Modifier,
    label: String,
    selectedOption: String,
    options: ImmutableList<String>,
    onSelectOption: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val commonModifier = Modifier.fillMaxWidth()

    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Box(
            modifier = commonModifier
        ) {
            ExposedDropdownMenuBox(
                modifier = commonModifier.clip(Shapes().medium),
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    value = selectedOption,
                    onValueChange = {},
                    readOnly = true,
                    shape = Shapes().medium,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = commonModifier.menuAnchor()
                )

                DropdownMenu(
                    modifier = Modifier.exposedDropdownSize(),
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { item ->
                        DropdownMenuItem(
                            modifier = commonModifier,
                            text = { Text(text = item) },
                            onClick = {
                                onSelectOption(item)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun <T> LargeDropdownMenu(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    notSetLabel: String? = null,
    items: ImmutableList<T>,
    selectedItem: T? = null,
    onItemSelected: (index: Int, item: T) -> Unit,
    selectedItemToString: (T) -> String = { it.toString() },
    drawItem: @Composable (T, Boolean, Boolean, () -> Unit) -> Unit = { item, selected, itemEnabled, onClick ->
        LargeDropdownMenuItem(
            text = item.toString(),
            selected = selected,
            enabled = itemEnabled,
            onClick = onClick,
        )
    },
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }

    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Box(modifier = modifier.height(IntrinsicSize.Min)) {
            OutlinedTextField(
                value = selectedItem?.let(selectedItemToString) ?: "",
                enabled = enabled,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                onValueChange = { },
                readOnly = true,
                shape = Shapes().medium,
            )

            // Transparent clickable surface on top of OutlinedTextField
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(Shapes().medium)
                    .clickable(enabled = enabled) { expanded = true },
                color = Color.Transparent,
            ) {}
        }
    }

    if (expanded) {
        Dialog(onDismissRequest = { expanded = false }) {
            Surface(shape = RoundedCornerShape(12.dp)) {
                val listState = rememberLazyListState()
                if (selectedIndex > -1) {
                    LaunchedEffect("ScrollToSelected") {
                        listState.scrollToItem(index = selectedIndex)
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
                    if (notSetLabel != null) {
                        item {
                            LargeDropdownMenuItem(
                                text = notSetLabel,
                                selected = false,
                                enabled = false,
                                onClick = { },
                            )
                        }
                    }
                    itemsIndexed(items) { index, item ->
                        val isSelected = index == selectedIndex
                        drawItem(
                            item,
                            isSelected,
                            true
                        ) {
                            selectedIndex = index
                            onItemSelected(index, item)
                            expanded = false
                        }

                        if (index < items.lastIndex) {
                            Divider(modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LargeDropdownMenuItem(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5F)
        selected -> MaterialTheme.colorScheme.primary.copy(alpha = 1F)
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 1F)
    }

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(modifier = Modifier
            .clickable(enabled) { onClick() }
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}