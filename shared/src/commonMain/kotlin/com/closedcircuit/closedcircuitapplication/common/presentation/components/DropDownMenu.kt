@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.common.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import kotlinx.collections.immutable.ImmutableList


@Composable
fun TextFieldDropDownMenu(
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
        Box(modifier = commonModifier) {
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
                    modifier = commonModifier.menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
                        focusedContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
                        errorContainerColor = MaterialTheme.colorScheme.inverseOnSurface
                    )
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
